package com.example.evm_2.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "voter")
public class Voter {
    String email;
    String cnic;
    String password;

    public Voter() {
    }

    public Voter(String email, String cnic, String password, String name) {
        this.email = email;
        this.cnic = cnic;
        this.password = password;
        this.name = name;

    }

    String name;

    @Override
    public String toString() {
        return "Voter{" +
                "email='" + email + '\'' +
                ", cnic='" + cnic + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @DynamoDBHashKey(attributeName = "cnic")
    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @DynamoDBRangeKey(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
