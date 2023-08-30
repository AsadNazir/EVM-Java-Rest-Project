package com.example.evm_2.resources;

import com.example.evm_2.domain.Admin;
import com.example.evm_2.domain.Voter;
import com.example.evm_2.services.AuthService;
import com.example.evm_2.services.Scheduler;
import com.example.evm_2.services.VoterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import com.example.evm_2.commons.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


//Used for Auth Services such as Jwt and registration
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
    public Response register(String Jsondata) {

        Voter voter;
        try {
            if (VoterService.getInstance().isVotingQueueReady()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(objectMapper.writeValueAsString(new CustomResponse(true, "Registration is closed now"))).build();
            }

            voter = objectMapper.readValue(Jsondata, Voter.class);
            CustomResponse res = (CustomResponse) AuthService.getInstance().Register(voter);

            return Response.status(Response.Status.OK).entity(objectMapper.writeValueAsString(res)).build();

        } catch (Exception E) {
            E.printStackTrace();
        }

        return null;
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/adminLogin")
    public Response adminLogin(String Jsondata) {

        try {

            Admin admin = null;

            admin = objectMapper.readValue(Jsondata, Admin.class);

            return Response.status(Response.Status.OK).entity(objectMapper.writeValueAsString(AuthService.getInstance().AdminLogin(admin))).build();

        } catch (Exception E) {
            E.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}