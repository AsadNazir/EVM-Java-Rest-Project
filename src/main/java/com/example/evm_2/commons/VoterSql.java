package com.example.evm_2.commons;

public abstract class VoterSql {

    public static final String GET_VOTER_BY_CNIC="SELECT * FROM voters WHERE CNIC = ?";
    public static final String REGISTER ="INSERT INTO evm.voter (cnic, name, password, email) VALUES (?, ?, ?, ?)";
}
