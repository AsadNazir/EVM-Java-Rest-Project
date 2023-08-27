package com.example.evm_2.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.example.evm_2.commons.DynamoDb;
import com.example.evm_2.domain.Voter;
import com.amazonaws.services.dynamodbv2.document.*;


import java.util.List;

//Voter Service for all the Db related work
public class VoterService {

    private static VoterService vS;

    private VoterService() {
    }

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

    public boolean addVoter(Voter voter)
    {
        try {
            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();
            DynamoDB db = new DynamoDB(amazonDynamoDB);

            Table T= db.getTable("voter");

            Item I = new Item().withPrimaryKey("cnic", voter.getCnic())
                    .withString("email", voter.getEmail())
                    .withString("password", voter.getPassword())
                    .withString("name", voter.getName());

            T.putItem(I);

            return true;
        }
        catch (Exception e)
        {
            System.out.println("Occurred in addVoter Voter Service");
            e.printStackTrace();
        }

        return false;

    }



}
