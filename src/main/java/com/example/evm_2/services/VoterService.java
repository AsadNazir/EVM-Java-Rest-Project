package com.example.evm_2.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.example.evm_2.commons.DbOperations;
import com.example.evm_2.commons.DynamoDb;
import com.example.evm_2.domain.Vote;
import com.example.evm_2.domain.Voter;
import com.amazonaws.services.dynamodbv2.document.*;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.List;

//Voter Service for all the Db related work
public class VoterService {

    private static VoterService vS;
    private final DbOperations dbOperations;
    private VoterService() {
        this.dbOperations = new DbOperations();
    }

    //Singleton pattern
    public static VoterService getInstance() {
        if (vS == null) {
            synchronized (VoterService.class) {
                if (vS == null) {
                    vS = new VoterService();
                }
            }
        }
        return vS;
    }

    public List<Voter> getAllVoters()
    {
        try {

            List<Voter> voterList = new ArrayList<>();

           List<Item> voterItems= dbOperations.getAllEntries("voter");

           for (Item I :voterItems)
           {
               voterList.add(new Voter(I.getString("email"),I.getString("cnic"),I.getString("password"),I.getString("name")));
           }
            return voterList;
        }
        catch (Exception E)
        {
            E.printStackTrace();
            return null;
        }
    }

    //Check if user has already cast a vote or not
    public boolean HasCastTheVote(String cnic) {
        try {
            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();

            Vote V = new Vote();
            V.setCnic(cnic);

            V.setCnic(cnic);
            DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
            DynamoDBQueryExpression<Vote> queryExpression = new DynamoDBQueryExpression<Vote>()
                    .withHashKeyValues(V);

            List<Vote> itemList = mapper.query(Vote.class, queryExpression);

            //if already exists false else true
            return !itemList.isEmpty();


        } catch (Exception E) {
            E.printStackTrace();
        }

        return true;
    }


    //To check if voting has started or not
    public boolean isVotingQueueReady() {
        BasicAWSCredentials credentials = new BasicAWSCredentials("asad", "asad");
        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .build();

        try {
            String queueName = "voting";
            GetQueueUrlResult queueUrlResult = sqs.getQueueUrl(new GetQueueUrlRequest(queueName));
            return true; // Queue exists
        } catch (com.amazonaws.services.sqs.model.QueueDoesNotExistException e) {
            // Queue doesn't exist
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Error occurred
        }
    }

    //Delete a voter
    public boolean deleteVoter(String cnic, String name)
    {
        try {
            return dbOperations.deleteItem("cnic",cnic,"voter","name", name);
        }
        catch (Exception E)
        {
            E.printStackTrace();
        }
        return false;

    }


    //Casting vote
    public boolean castVote(Vote vote) {
        try {

            //if he/she has cast the vote then return false
            if (HasCastTheVote(vote.getCnic())) {
                return false;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            SqsService.getInstance().sendMsg("voting", objectMapper.writeValueAsString(vote));
            new Scheduler().pushVotesToDb(List.of(vote));

            return true;
        } catch (Exception E) {
            E.printStackTrace();

        }

        return false;
    }


    //Getting voter by CNIC
    public Voter getVoterByCnic(String cnic, String name) {
        try {
            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();

            Voter V = new Voter();

            V.setCnic(cnic);
            DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
            DynamoDBQueryExpression<Voter> queryExpression = new DynamoDBQueryExpression<Voter>()
                    .withHashKeyValues(V);

            List<Voter> itemList = mapper.query(Voter.class, queryExpression);

            if (itemList.isEmpty())
                return null;

            else return itemList.get(0);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // If no voter with the given CNIC is found
    }

    public boolean addVoter(Voter voter) {
        try {
            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();
            DynamoDB db = new DynamoDB(amazonDynamoDB);

            Table T = db.getTable("voter");

            Item I = new Item().withPrimaryKey("cnic", voter.getCnic())
                    .withString("email", voter.getEmail())
                    .withString("password", voter.getPassword())
                    .withString("name", voter.getName());

            T.putItem(I);

            return true;
        } catch (Exception e) {
            System.out.println("Occurred in addVoter Voter Service");
            e.printStackTrace();
        }

        return false;

    }


}
