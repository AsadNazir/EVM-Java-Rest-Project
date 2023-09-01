package com.example.evm_2.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.example.evm_2.commons.DbOperations;
import com.example.evm_2.commons.DynamoDb;
import com.example.evm_2.commons.CustomResponse;
import com.example.evm_2.domain.Party;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PartyService {
    private static PartyService instance;
    private final DbOperations dbOperations;

    // Private constructor to prevent instantiation from outside
    private PartyService() {
        dbOperations = new DbOperations();
    }

    // Static method to get the singleton instance
    public static PartyService getInstance() {
        if (instance == null) {
            synchronized (AuthService.class) {
                if (instance == null) {
                    instance = new PartyService();
                }
            }
        }
        return instance;
    }


    //delete a party
    public boolean deleteParty(String regNo) {
        try {

            System.out.println(regNo);
            return dbOperations.deleteItem("regNo", regNo, "Party");

        } catch (Exception E) {
            E.printStackTrace();

        }

        return false;
    }


    public List<Party> getAllParties() {
        List<Party> parties = new ArrayList<>();

        try {
            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();
            DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);

            ScanRequest scanRequest = new ScanRequest().withTableName("Party"); // Table name

            ScanResult scanResult = amazonDynamoDB.scan(scanRequest);

            for (Map<String, AttributeValue> item : scanResult.getItems()) {
                Party party = mapper.marshallIntoObject(Party.class, item);
                parties.add(party);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return parties;
    }

    public Object addParty(Party party) {
        try {
//            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();
//            DynamoDB db = new DynamoDB(amazonDynamoDB);
//
//            Table T = db.getTable("Party");
//
            Item I = new Item().withPrimaryKey("regNo", party.getRegNo()).withString("partyName", party.getPartyName()).withString("partyLeader", party.getPartyLeader()).withString("electionSign", party.getElectionSign());
//
//            var t = T.putItem(I);

            if (dbOperations.putData("Party", List.of(I)))
                return new CustomResponse(false, "added successfully");


        } catch (Exception E) {
            E.printStackTrace();

        }

        return new CustomResponse(false, "Error Occurred");

    }


}
