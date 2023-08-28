package com.example.evm_2.services;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.List;

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

    public boolean sendMsg(String queueName, String msgBody) {
        try {
            String queueUrl = sqs.getQueueUrl(new GetQueueUrlRequest(queueName)).getQueueUrl();
            SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, msgBody);
            sqs.sendMessage(sendMessageRequest);
            System.out.println("Message sent to " + queueName + ": " + msgBody);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Message> getAllMessages(String queueName) {
        String queueUrl = sqs.getQueueUrl(new GetQueueUrlRequest(queueName)).getQueueUrl();
        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();
        return messages;
    }

    public void close() {
        sqs.shutdown();
    }

}
