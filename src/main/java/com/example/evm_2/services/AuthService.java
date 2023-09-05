package com.example.evm_2.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.evm_2.commons.*;
import com.example.evm_2.domain.Admin;
import com.example.evm_2.domain.Voter;

public class AuthService {
    private static AuthService instance;

    // Private constructor to prevent instantiation from outside
    private AuthService() {
    }

    // Static method to get the singleton instance
    public static AuthService getInstance() {
        if (instance == null) {
            synchronized (AuthService.class) {
                if (instance == null) {
                    instance = new AuthService();
                }
            }
        }
        return instance;
    }

    public Object AdminLogin(Admin admin) {
        try {
            if (admin.getName().equalsIgnoreCase("admin") && admin.getPassword().equals("admin")) {
                JWTCreator.Builder jwtBuilder = JWT.create()
                        .withClaim("name", admin.getName())
                        .withSubject("admin")
                        .withIssuer("Evm");

                return new CustomResponse(false, jwtBuilder.sign(Credentials.SECRET_KEY_JWT));
            }
        } catch (Exception E) {
            E.printStackTrace();
            return new CustomResponse(true, "Some Error Occurred");
        }
        return new CustomResponse(false, "Wrong Credentials");
    }


    public boolean isVerified(String jwtToken) {
        //Takes the Header Authorization and just returns the payload
        try {
            DecodedJWT decodedJWT = JWT.require(Credentials.SECRET_KEY_JWT)
                    .build()
                    .verify(jwtToken);
            return decodedJWT.getIssuer().equalsIgnoreCase("Evm");
        } catch (Exception E) {
            E.printStackTrace();
            return false;
        }

    }

    public boolean isVoter(String jwtToken)
    {
        try {
            DecodedJWT decodedJWT = JWT.require(Credentials.SECRET_KEY_JWT)
                    .build()
                    .verify(jwtToken);

            return decodedJWT.getSubject().equalsIgnoreCase("voter");
        } catch (Exception E) {
            E.printStackTrace();
            return false;
        }
    }


    public boolean isAdmin(String jwtToken) {
        try {//Takes the Header Authorization and just returns the payload
            DecodedJWT decodedJWT = JWT.require(Credentials.SECRET_KEY_JWT)
                    .build()
                    .verify(jwtToken);
            return decodedJWT.getSubject().equalsIgnoreCase("admin");
        } catch (Exception E) {
            E.printStackTrace();
            return false;
        }
    }


    public Object Register(Voter voter) {
        try {
            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();


            if (voter.getName() == null || voter.getCnic() == null || voter.getCnic().length() < 11 || voter.getPassword() == null || voter.getEmail() == null) {
                return new CustomResponse(true, "Invalid or Incomplete Credentials");
            }

            Voter v = VoterService.getInstance().getVoterByCnic(voter.getCnic(), voter.getName());
            if (v != null) {
                return new CustomResponse(false, "You have already Registered !");
            }

            boolean result = VoterService.getInstance().addVoter(voter);
            boolean emailSent = EmailService.getInstance().sendMail("bsef20m522@pucit.edu.pk", "Hi " + voter.getName() + "\n\nYou have been successfully registered!\n\n Thanks !");


            if (result && emailSent)
                return new CustomResponse(false, "You have been registered successfully");


        } catch (Exception E) {
            E.printStackTrace();
        }


        return new CustomResponse(true, "Error Occurred while registering");
    }


    public Object Login(Voter voter) {

        try {
            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();


            Voter v = VoterService.getInstance().getVoterByCnic(voter.getCnic(), voter.getName());
            if (v == null) {
                return new CustomResponse(true, "Invalid Credentials");
            }


            JWTCreator.Builder jwtBuilder = JWT.create()
                    .withClaim("cnic", voter.getCnic())
                    .withClaim("email", voter.getEmail())
                    .withClaim("name", voter.getName())
                    .withSubject("voter")
                    .withIssuer("Evm");

            return new CustomResponse(false, jwtBuilder.sign(Credentials.SECRET_KEY_JWT));


        } catch (Exception E) {
            E.printStackTrace();
        }

        return new CustomResponse(true, "Error Occurred while Authenticating ");
    }


}
