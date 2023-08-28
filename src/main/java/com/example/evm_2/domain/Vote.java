package com.example.evm_2.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Vote")
public class Vote {
    String party;
    @DynamoDBHashKey
    String cnic;

    public Vote() {
    }

    public Vote(String party, String cnic, String time) {
        this.party = party;
        this.cnic = cnic;
        this.time = time;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    String time;
}
