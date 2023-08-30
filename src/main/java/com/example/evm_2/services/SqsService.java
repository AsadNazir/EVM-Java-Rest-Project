package com.example.evm_2.services;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

import java.util.List;

//For handling all the CRUD operations from and on SQS
public class SqsService {

    private static SqsService instance;

    private final AmazonSQS sqs;

    private SqsService() {
        sqs = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("asad", "asad")))
                .build();
    }

    public static synchronized SqsService getInstance() {
        if (instance == null) {
            instance = new SqsService();
        }
        return instance;
    }

    public void createQueue(String queueName) {
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName);
        sqs.createQueue(createQueueRequest);
        System.out.println("Queue created: " + queueName);
    }

    public void sendMsg(String queueName, String msgBody) {
        try {
            String queueUrl = sqs.getQueueUrl(new GetQueueUrlRequest(queueName)).getQueueUrl();
            SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, msgBody);
            sqs.sendMessage(sendMessageRequest);
            System.out.println("Message sent to " + queueName + ": " + msgBody);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Message> getAllMessages(String queueName) {
        String queueUrl = sqs.getQueueUrl(new GetQueueUrlRequest(queueName)).getQueueUrl();

        return sqs.receiveMessage(queueUrl).getMessages();
    }

    public void close() {
        sqs.shutdown();
    }

    public void deleteMessage(String queueName, String receiptHandle) {
        try {
            String queueUrl = sqs.getQueueUrl(new GetQueueUrlRequest(queueName)).getQueueUrl();
            sqs.deleteMessage(new DeleteMessageRequest(queueUrl, receiptHandle));
            System.out.println("Message deleted: " + receiptHandle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteQueue(String queueName) {
        try {
            String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
            sqs.deleteQueue(new DeleteQueueRequest(queueUrl));
            System.out.println("Queue deleted: " + queueName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
