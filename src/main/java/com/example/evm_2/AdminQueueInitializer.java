package com.example.evm_2;

import com.example.evm_2.services.SqsService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AdminQueueInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        // Initialize SQS client with custom credentials
//        BasicAWSCredentials credentials = new BasicAWSCredentials("asad", "asad");
//        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-2"))
//                .build();
//
//        // Create the "Admin" queue
//        CreateQueueRequest createQueueRequest = new CreateQueueRequest("Admin");
//        sqs.createQueue(createQueueRequest);
//
//        System.out.println("Admin Queue created on server startup.");
//        sqs.shutdown();

        SqsService.getInstance().createQueue("admin");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nothing to do when the context is destroyed
    }
}
