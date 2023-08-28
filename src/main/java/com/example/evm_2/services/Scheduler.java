package com.example.evm_2.services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    String votingQueueUrl;
    private final SqsService sqsService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public Scheduler() {
      sqsService = SqsService.getInstance();
    }

    public void startTheVoting() {
        try {

         sqsService.createQueue("voting");
         sqsService.sendMsg("admin","Voting Started");

            scheduler.schedule(this::Task, 20, TimeUnit.SECONDS);
        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    private void Task() {

        List<Message> messages = sqsService.getAllMessages("admin");

        for (Message message : messages) {
            System.out.println("Received message: " + message.getBody());

        }

        System.out.println("Task executed after 30 minutes.");
    }

    public void FinishTheVoting() {
        scheduler.shutdown();
    }
}
