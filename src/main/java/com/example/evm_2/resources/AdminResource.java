package com.example.evm_2.resources;

import com.example.evm_2.domain.Party;
import com.example.evm_2.services.AuthService;
import com.example.evm_2.services.PartyService;
import com.example.evm_2.services.Scheduler;
import com.example.evm_2.services.VoterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import com.example.evm_2.commons.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/admin")
public class AdminResource {
    ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addParty")
    public Response addParty(@HeaderParam("Authorization") String authorizationHeader, String party) {
        try {
            if (this.isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //Checking if voting has started or not
            if (VoterService.getInstance().isVotingQueueReady()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Voting has satrted cannot add more Parties")).build();
            }

            Party P = objectMapper.readValue(party, Party.class);
            return Response.status(200).entity(objectMapper.writeValueAsString(PartyService.getInstance().addParty(P))).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllParties")
    public Response getAllParties(@HeaderParam("Authorization") String authorizationHeader) {
        try {

            if (!this.isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            List<Party> L = PartyService.getInstance().getAllParties();

            return Response.status(200).entity(objectMapper.writeValueAsString(new CustomResponse(false, L))).build();

        } catch (Exception E) {

            E.printStackTrace();
        }

        return null;
    }

    @POST
    @Path("/startVoting")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response startVoting(@HeaderParam("Authorization") String authorizationHeader) {
        try {
            if (!isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //if voting has started or not
            if (VoterService.getInstance().isVotingQueueReady()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Voting has already been started")).build();
            }
            Scheduler scheduler = new Scheduler();

            //Starting the voting by taking time input in seconds
            scheduler.startTheVoting(40);
            return Response.status(200).entity(objectMapper.writeValueAsString(new CustomResponse(false, "Voting has been started"))).build();
        } catch (Exception E) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error Occurred Voting cannot be started").build();
        }
    }

    @GET
    @Path("/getVoteCount")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVoteCount(@HeaderParam("Authorization") String authorizationHeader) {
        try {
            if (!isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //if voting has started or not
            if (!VoterService.getInstance().isVotingQueueReady()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Voting has already been started")).build();
            }
            Scheduler scheduler = new Scheduler();
            return Response.status(200).entity(objectMapper.writeValueAsString(new CustomResponse(false, scheduler.getAllVoteCount()))).build();
        } catch (Exception E) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error Occurred Voting cannot be started").build();
        }
    }


    //Function to verify only admin has the access
    private boolean isAdmin(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return false;
        }


        String jwtToken = authorizationHeader.substring("Bearer ".length());
        return AuthService.getInstance().isVerified(jwtToken) && AuthService.getInstance().isAdmin(jwtToken);
    }

}
