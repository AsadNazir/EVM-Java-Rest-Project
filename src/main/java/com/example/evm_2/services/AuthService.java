package com.example.evm_2.services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.evm_2.commons.*;
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

//    public Object Register(Voter voter) {
//
//        try (Connection C = SqlDb.getConnection();
//             PreparedStatement Ps = C.prepareStatement(VoterSql.REGISTER);
//        ) {
//            // Set values for the parameters
//            Ps.setString(1, voter.getCnic());
//            Ps.setString(2, voter.getName());
//            Ps.setString(3, voter.getPassword());
//            Ps.setString(4, voter.getEmail());
//
//            int affected = Ps.executeUpdate();
//
//            if (affected > 0) {
//                return new Response(true, "Successfully registered.");
//            } else {
//                return new Response(false, "Registration failed.");
//            }
//
//        } catch (Exception E) {
//            E.printStackTrace();
//        }
//
//
//        return new Response(true, null);
//    }


//    public Object Login(Voter voter) {
//
//
//        ArrayList<Voter> V = new ArrayList<>();
//
//        try (Connection C = SqlDb.getConnection();
//             PreparedStatement Ps = C.prepareStatement(VoterSql.GET_VOTER_BY_CNIC);
//        ) {
//            Ps.setString(1, voter.getCnic());
//
//            try (ResultSet rs = Ps.executeQuery()) {
//
//                while (rs.next()) {
//                    String cnic = rs.getString("cnic");
//                    String name = rs.getString("name");
//                    String pwd = rs.getString("password");
//                    String email = rs.getString("email");
//
//                    V.add(new Voter(email, cnic, pwd, name));
//                }
//
//                if (V.isEmpty())
//                    return new Response(true, null);
//
//                JWTCreator.Builder jwtBuilder = JWT.create()
//                        .withClaim("cnic", "123")
//                        .withClaim("email", voter.getEmail())
//                        .withClaim("name", voter.getName())
//                        .withSubject("voter")
//                        .withIssuer("EVM");
//
//                return new Response(true, jwtBuilder.sign(Credentials.SECRET_KEY_JWT));
//            }
//        } catch (Exception E) {
//            E.printStackTrace();
//        }
//
//
//        return new Response(true, null);
//    }


    public boolean isVerified(String jwtToken) {
        //Takes the Header Authorization and just returns the payload
        DecodedJWT decodedJWT = JWT.require(Credentials.SECRET_KEY_JWT)
                .build()
                .verify(jwtToken);

        if (decodedJWT.getSubject().equalsIgnoreCase("admin"))
            return true;

        return false;
    }

    public Object Register(Voter voter) {
        try {
            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();


            if (voter.getName() == null || voter.getCnic() == null || voter.getCnic().length() < 11 || voter.getPassword() == null || voter.getEmail() == null) {
                return new Response(true, "Invalid or Incomplete Credentials");
            }

            Voter v = VoterService.getInstance().getVoterByCnic(voter.getCnic(), voter.getName());
            if (v != null) {
                return new Response(false, "You have already Registered !");
            }

            boolean result = VoterService.getInstance().addVoter(voter);
            boolean emailSent = EmailService.getInstance().sendMail("bsef20m522@pucit.edu.pk", "Hi " + voter.getName() + "\n\nYou have been successfully registered!\n\n Thanks !");


            if (result && emailSent)
                return new Response(false, "You have been registered successfully");


        } catch (Exception E) {
            E.printStackTrace();
        }


        return new Response(true, "Error Occurred while registering");
    }


    public Object Login(Voter voter) {

        try {
            AmazonDynamoDB amazonDynamoDB = DynamoDb.getInstance();


            Voter v = VoterService.getInstance().getVoterByCnic(voter.getCnic(), voter.getName());
            if (v == null) {
                return new Response(false, "Invalid Credentials");
            }


            JWTCreator.Builder jwtBuilder = JWT.create()
                    .withClaim("cnic", "123")
                    .withClaim("email", voter.getEmail())
                    .withClaim("name", voter.getName())
                    .withSubject("voter")
                    .withIssuer("EVM");

            return new Response(false, jwtBuilder.sign(Credentials.SECRET_KEY_JWT));


        } catch (Exception E) {
            E.printStackTrace();
        }

        return new Response(true, "Error Occurred while Authenticating ");
    }


}
