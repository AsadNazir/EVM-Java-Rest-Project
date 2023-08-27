package com.example.evm_2.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Party")
public class Party {

    String partyName;
    String partyLeader;
    @DynamoDBHashKey
    String regNo;

    @Override
    public String toString() {
        return "Party{" +
                "partName='" + partyName + '\'' +
                ", partyLeader='" + partyLeader + '\'' +
                ", regNo='" + regNo + '\'' +
                ", electionSign='" + electionSign + '\'' +
                '}';
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyLeader() {
        return partyLeader;
    }

    public void setPartyLeader(String partyLeader) {
        this.partyLeader = partyLeader;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getElectionSign() {
        return electionSign;
    }

    public void setElectionSign(String electionSign) {
        this.electionSign = electionSign;
    }

    public Party() {
    }

    String electionSign;

}
