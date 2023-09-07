package com.example.evm_2.services;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.amazonaws.services.sqs.model.*;
import com.example.evm_2.commons.DbOperations;
import com.example.evm_2.commons.DynamoDb;
import com.example.evm_2.domain.Party;
import com.example.evm_2.domain.Vote;
import com.example.evm_2.domain.VoteCounts;
import com.example.evm_2.domain.VotingTime;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//Scheduler Class responsible for handling schedule of result announcement
public class Scheduler {

    private final SqsService sqsService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Scheduler() {
        sqsService = SqsService.getInstance();
    }


    //Fucntion which starts the voting
    public void startTheVoting(int time) {
        try {


            ObjectMapper objectMapper = new ObjectMapper();
            //Deleting the Party Votes Table First and then creating it again
            DbOperations dbOperations = new DbOperations();
            if (dbOperations.tableExists("PartyVotes")) {
                if (dbOperations.deleteTable("PartyVotes")) {
                    System.out.println("PartyVotes Deleted");
                }
            }

            //Creating the Party Votes Table Again
            if (dbOperations.CreateTable(List.of(new KeySchemaElement("party", KeyType.HASH)), List.of(new AttributeDefinition("party", ScalarAttributeType.S)), "PartyVotes")) {
                System.out.println("PartyVotes Created");
            }


            //Adding 0 Entries to Party Votes

            List<Party> parties = PartyService.getInstance().getAllParties();

            //Initializing all parties in Db with 0 votes when voting begun
            List<Item> ItemList = new ArrayList<>();
            for (Party P : parties) {
                Item I = new Item().withPrimaryKey("party", P.getRegNo())
                        .withNumber("votes", 0);
                ItemList.add(I);

            }

            if (dbOperations.putData("PartyVotes", ItemList)) {
                System.out.println("Party Votes has been initialised with 0");
            }

            //if Admin Queue contains some message delete it first
            List<Message> Msg = sqsService.getAllMessages("admin");
            if (!Msg.isEmpty()) {
                sqsService.deleteMessage("admin", Msg.get(0).getReceiptHandle());
            }

            dbOperations.deleteAllEntries("votes","cnic");


            //Starting the Voting Queue
            sqsService.createQueue("voting");

            //Pushing the message the voting has started
            sqsService.sendMsg("admin", new Date().toString());

            //Starting the Scheduler to run the Task Function after exactly time seconds
            scheduler.schedule(this::Task, time, TimeUnit.SECONDS);

            // Get the current time
            LocalDateTime now = LocalDateTime.now();

            // Add minutes to the current time
            LocalDateTime updatedTime = now.plus(time, ChronoUnit.MINUTES);

            Item item = new Item().withPrimaryKey("startingTime", now.toString()).withString("endingTime", updatedTime.toString());
            boolean votingTimes = dbOperations.putData("votingTime", List.of(item));


        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    public VotingTime getVotingTime() {
        try {
            DbOperations dbOperations = new DbOperations();
            List<Item> itemList = dbOperations.getAllEntries("votingTime");

            if (itemList.isEmpty()) return null;

            return new VotingTime(itemList.get(0).getString("startingTime"),
                    itemList.get(0).getString("endingTime"));


        } catch (Exception E) {
            E.printStackTrace();
            return null;
        }
    }

    private void Task() {

        //This will count the votes and push all the votes from the SQS to db
        getVoteCount();
        getAllVoteCount();
        //Deleting the voting SQS after the use to indicate voting has stopped
        SqsService.getInstance().deleteQueue("voting");
    }


    public List<Vote> CountVote() {

        //Here is the logic for counting all the votes
        List<Vote> AllVotes = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (true) {
                List<Message> messages = SqsService.getInstance().getAllMessages("voting");

                System.out.println("Size: " + messages.size());

                if (messages.isEmpty()) {
                    System.out.println("No messages left. Stopping the task in count vote");
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

    public void getVoteCount() {
        DbOperations dbOperations = new DbOperations();

        List<Vote> votes = CountVote();
        Map<String, Integer> voteCount = new HashMap<>();
        List<Item> updatedVoteCounts = new ArrayList<>();

        if (votes.isEmpty()) {
            System.out.println("this list is empty");
        }

        for (Vote V : votes) {
            if (!voteCount.containsKey(V.getParty())) {
                voteCount.put(V.getParty(), 1);
            } else {
                voteCount.put(V.getParty(), voteCount.get(V.getParty()) + 1);
            }
        }

        for (Map.Entry<String, Integer> entry : voteCount.entrySet()) {
            Item voteItem = new Item().withPrimaryKey("party", entry.getKey())
                    .withNumber("votes", entry.getValue());

            updatedVoteCounts.add(voteItem);

            // Get old vote count from DynamoDB
            Item oldVoteCount = dbOperations.getItemFromTable(entry.getKey(), "PartyVotes", "party");
            BigDecimal oldVoteCountVal = oldVoteCount.getNumber("votes");

            // Calculate new vote count
            int newVoteCountVal = entry.getValue() + oldVoteCountVal.intValue();

            // Update the vote count in DynamoDB
            dbOperations.updateTableItem(
                    "PartyVotes",
                    "party",
                    entry.getKey(),
                    "votes",
                    String.valueOf(newVoteCountVal)
            );

            System.out.println(entry.getKey() + ": " + newVoteCountVal);
        }

    }

    public List<VoteCounts> getAllVoteCount() {
        DbOperations dbOperations = new DbOperations();
        if (VoterService.getInstance().isVotingQueueReady()) {
            getVoteCount();
        }

        List<Item> itemList = dbOperations.getAllEntries("PartyVotes");
        List<VoteCounts> voteCountsList = new ArrayList<>();

        for (Item I : itemList) {
            voteCountsList.add(new VoteCounts(I.getString("party"), I.getNumber("votes").intValue()));
        }

        return voteCountsList;
    }


    //Function to push votes to the db takes a Vote List
    public void pushVotesToDb(List<Vote> votes) {

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

    }


    //Manually shutting down the scheduler
    public void FinishTheVoting() {

        //Calling the Task and then shutting down
        // Calling the Task and then shutting down
        Task();

        // Shut down the scheduler
        scheduler.shutdown();

        // Optionally, you can wait for the scheduler to terminate
        try {
            if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                // Handle the case where the scheduler did not terminate in 60 seconds
                // You can log an error or take appropriate action here.
                System.out.println("Scheduler did not shut down");
            }
        } catch (InterruptedException e) {
            // Handle the InterruptedException
            e.printStackTrace();
        }
    }
}
