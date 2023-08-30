package com.example.evm_2.services;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.sqs.model.*;
import com.example.evm_2.commons.DynamoDb;
import com.example.evm_2.domain.Party;
import com.example.evm_2.domain.Vote;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//Scheduler Class responsible for handling schedule of result announcement
public class Scheduler {

    String votingQueueUrl;
    private final SqsService sqsService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Scheduler() {
        sqsService = SqsService.getInstance();
    }


    //Fucntion which starts the voting
    public void startTheVoting(int time) {
        try {

            //Adding 0 Entries to Party Votes
            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();
            DynamoDB db = new DynamoDB(amazonDynamoDB);

            Table T = db.getTable("PartyVotes");

            List<Party> parties = PartyService.getInstance().getAllParties();

            //Initializing all parties in Db with 0 votes when voting begun
            for (Party P : parties) {
                Item I = new Item().withPrimaryKey("party", P.getRegNo())
                        .withNumber("votes", 0);

                T.putItem(I);
            }

            //if Admin Queue conatains some message delete it first
            List<Message> Msg = sqsService.getAllMessages("admin");
            if (!Msg.isEmpty()) {
                sqsService.deleteMessage("admin", Msg.get(0).getReceiptHandle());
            }


            //Starting the Voting Queue
            sqsService.createQueue("voting");

            //Pushing the message the voting has started
            sqsService.sendMsg("admin", "Voting Started");

            //Starting the Scheduler to run the Task Function after exactly time seconds
            scheduler.schedule(this::Task, 30, TimeUnit.SECONDS);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    private void Task() {

        //This will count the votes and push all the votes from the SQS to db
        getVoteCount();

        //Deleting the voting SQS after the use to indicate voting has stopped
        SqsService.getInstance().deleteQueue("voting");
    }


    private List<Vote> CountVote() {

        //Here is the logic for counting all the votes
        List<Vote> AllVotes = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (true) {
                List<Message> messages = SqsService.getInstance().getAllMessages("voting");

                System.out.println("Size: " + messages.size());

                if (messages.isEmpty()) {
                    System.out.println("No messages left. Stopping the task.");
                    break; // Exit the loop when there are no more messages
                }

                for (Message message : messages) {

                    Vote v = objectMapper.readValue(message.getBody(), Vote.class);
                    AllVotes.add(v);

                    String receiptHandle = message.getReceiptHandle();
                    sqsService.deleteMessage("voting", receiptHandle);
                }
            }

            pushVotesToDb(AllVotes);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return AllVotes;
    }

    public Map<String, Integer> getVoteCount() {

        //This just simply returns the vote count in form of Map
        //Party1 : voteCount1
        //Part2 : voteCount2

        List<Vote> votes = CountVote();
        Map<String, Integer> voteCount = new HashMap<>();

        for (Vote V : votes) {
            if (!voteCount.containsKey(V.getParty())) {
                voteCount.put(V.getParty(), 1);
            } else {
                voteCount.put(V.getParty(), voteCount.get(V.getParty()) + 1);
            }
        }

        //Write the code to display the map here
        for (Map.Entry<String, Integer> entry : voteCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return voteCount;
    }


    //Function to push votes to the db takes a Vote List
    public boolean pushVotesToDb(List<Vote> votes) {


        try {
            for (Vote V : votes) {
                AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();
                DynamoDB db = new DynamoDB(amazonDynamoDB);

                Table T = db.getTable("votes");

                Item I = new Item().withPrimaryKey("cnic", V.getCnic()).withString("party", V.getParty()).withString("time", V.getTime());

                var t = T.putItem(I);
            }
        } catch (Exception E) {
            E.printStackTrace();
        }

        return false;
    }


    //Manually shutting down the scheduler
    public void FinishTheVoting() {

        //Calling the Task and then shutting down
        Task();
        scheduler.shutdown();
    }
}
