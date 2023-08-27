package com.example.evm_2.resources;

import com.example.evm_2.domain.Voter;
import com.example.evm_2.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import com.example.evm_2.commons.*;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
public class AuthResource {

    ObjectMapper objectMapper = new ObjectMapper();
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public String login(String Jsondata) {

        Voter voter = null;
        try {
            voter = objectMapper.readValue(Jsondata, Voter.class);

            return
            objectMapper.writeValueAsString(AuthService.getInstance().Login(voter));

        } catch (Exception E) {
            E.printStackTrace();
        }

        return null;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/register")
    public String register(String Jsondata) {

        Voter voter;
        try {
            voter = objectMapper.readValue(Jsondata, Voter.class);
            Response res = (Response) AuthService.getInstance().Register(voter);

            return objectMapper.writeValueAsString(res);

        } catch (Exception E) {
            E.printStackTrace();
        }

        return null;
    }

}