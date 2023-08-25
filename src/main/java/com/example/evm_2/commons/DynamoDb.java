package com.example.evm_2.commons;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class DynamoDb {

    private static volatile AmazonDynamoDB instance;

    private DynamoDb() {
        // Private constructor to prevent instantiation
    }

    public static AmazonDynamoDB getInstance() {
        if (instance == null) {
            synchronized (DynamoDb.class) {
                if (instance == null) {
                    instance = createDynamoDbInstance();
                }
            }
        }
        return instance;
    }

    private static AmazonDynamoDB createDynamoDbInstance() {
        String serviceEndpoint = "http://localhost:9999"; // DynamoDB Local endpoint
        String signingRegion = "us-east-1"; // Replace with the appropriate region

        BasicAWSCredentials credentials = new BasicAWSCredentials("498kk", "v7575h"); // Replace with your credentials

        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion))
                .withCredentials( new AWSStaticCredentialsProvider(credentials))
                .build();
    }
}
