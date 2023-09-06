package com.example.evm_2.resources;

import com.amazonaws.services.sqs.model.Message;
import com.example.evm_2.domain.Admin;
import com.example.evm_2.domain.Party;
import com.example.evm_2.domain.VotingTime;
import com.example.evm_2.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import com.example.evm_2.commons.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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
            if (!this.isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //Checking if voting has started or not
            if (VoterService.getInstance().isVotingQueueReady()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Voting has started cannot add more Parties")).build();
            }

            Party P = objectMapper.readValue(party, Party.class);
            return Response.status(200).entity(objectMapper.writeValueAsString(PartyService.getInstance().addParty(P))).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteVoter")
    public Response deleteVoter(@HeaderParam("Authorization") String authorizationHeader, @QueryParam("cnic") String cnic, @QueryParam("name") String name) {
        try {
            if (!this.isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            //if voting has started or not
            if (VoterService.getInstance().isVotingQueueReady()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Voting has started cannot change voters")).build();
            }

            if (VoterService.getInstance().deleteVoter(cnic, name)) {
                return Response.status(Response.Status.OK).entity(new CustomResponse(false, "Voter Deleted Successfully")).build();
            }

        } catch (Exception E) {
            E.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Error Occurred while deleting")).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteParty")
    public Response deleteParty(@HeaderParam("Authorization") String authorizationHeader, @QueryParam("regNo") String regNo) {
        try {

            if (!this.isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            //if voting has started or not
            if (VoterService.getInstance().isVotingQueueReady()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Voting has started cannot change party members")).build();
            }

            if (PartyService.getInstance().deleteParty(regNo)) {
                return Response.status(Response.Status.OK).entity(new CustomResponse(false, "Party Deleted Successfully")).build();
            }


        } catch (Exception E) {
            E.printStackTrace();
        }
        return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Error Occurred while deleting")).build();
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

        return Response.status(Response.Status.BAD_REQUEST).entity("Error Occurred").build();
    }

    @GET
    @Path("/getVotingTime")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVotingTimes(@HeaderParam("Authorization") String authorizationHeader) {
        try {
            Scheduler scheduler = new Scheduler();
            if (!isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //if voting has started or not
            if (VoterService.getInstance().isVotingQueueReady()) {

                return Response.status(Response.Status.OK).entity(objectMapper.writeValueAsString(scheduler.getVotingTime())).build();
            }

            return Response.status(200).entity(new CustomResponse(true, "Voting has ended")).build();

        } catch (Exception E) {
            E.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Error Occurred Voting times could not be fetched")).build();
        }
    }

    @POST
    @Path("/startVoting")
    @Produces(MediaType.APPLICATION_JSON)
    public Response startVoting(@HeaderParam("Authorization") String authorizationHeader, @QueryParam("startVoting") int startVoting) {
        try {
            Scheduler scheduler = new Scheduler();
            if (!isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            //it means to stop voting
            if (startVoting == -99 && VoterService.getInstance().isVotingQueueReady()) {
                scheduler.FinishTheVoting();

                return Response.status(Response.Status.OK).entity(new CustomResponse(false, "Voting has been stopped")).build();
            }

            if (startVoting < 1) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Error Occurred voting can not be started")).build();
            }

            //if voting has started or not
            if (VoterService.getInstance().isVotingQueueReady()) {
                return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Voting has already been started")).build();
            }

            //Starting the voting by taking time input in Minutes
            scheduler.startTheVoting(startVoting);

            // Get the current time
            LocalDateTime now = LocalDateTime.now();
            // Add minutes to the current time
            LocalDateTime updatedTime = now.plus(startVoting, ChronoUnit.MINUTES);
            SqsService.getInstance().sendMsg("admin", objectMapper.writeValueAsString(new VotingTime(now.toString(), updatedTime.toString())));

            return Response.status(200).entity(objectMapper.writeValueAsString(new CustomResponse(false, "Voting has been started"))).build();
        } catch (Exception E) {
            E.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity(new CustomResponse(true, "Error Occurred voting can not be started")).build();
        }
    }

    @GET
    @Path("/isAdmin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyAdmin(@HeaderParam("Authorization") String authorizationHeader) {
        try {
            if (!isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            return Response.status(Response.Status.OK).entity(objectMapper.writeValueAsString(new CustomResponse(false, "Admin"))).build();

        } catch (Exception E) {
            E.printStackTrace();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();

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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllVoters")
    public Response getAllVoters(@HeaderParam("Authorization") String authorizationHeader) {
        try {
            if (!isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            return Response.status(200).entity(new CustomResponse(false, VoterService.getInstance().getAllVoters())).build();

        } catch (Exception E) {
            E.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("Error Occurred Voting cannot be started").build();
        }
    }

    @GET
    @Path("/isVotingReady")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isVotingReady(@HeaderParam("Authorization") String authorizationHeader) {
        try {
            if (!isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            return Response.status(Response.Status.OK).entity(objectMapper.writeValueAsString(new CustomResponse(false, VoterService.getInstance().isVotingQueueReady()))).build();

        } catch (Exception E) {
            E.printStackTrace();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();

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
