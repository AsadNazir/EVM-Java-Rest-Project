package com.example.evm_2.resources;
import com.example.evm_2.commons.CustomResponse;
import com.example.evm_2.domain.Vote;
import com.example.evm_2.services.AuthService;
import com.example.evm_2.services.VoterService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/voter")
public class VoterResource {

    ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/vote")
    public Response casteVote(@HeaderParam("Authorization") String authorizationHeader, String vote) {
        try {
            if (!this.isVoter(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            if (!VoterService.getInstance().isVotingQueueReady()) {
                return Response.status(Response.Status.FORBIDDEN).entity(objectMapper.writeValueAsString(new CustomResponse(true, "Voting Has not started yet"))).build();
            }

            Vote V = objectMapper.readValue(vote, Vote.class);
            return Response.status(200).entity(objectMapper.writeValueAsString(VoterService.getInstance().castVote(V))).build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    @GET
    @Path("/isVotingReady")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isVotingReady(@HeaderParam("Authorization") String authorizationHeader) {
        try {
            if (!isVoter(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            return Response.status(Response.Status.OK).entity(objectMapper.writeValueAsString(new CustomResponse(false, VoterService.getInstance().isVotingQueueReady()))).build();

        } catch (Exception E) {
            E.printStackTrace();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();

    }


    private boolean isVoter(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("here");
            return false;
        }

        String jwtToken = authorizationHeader.substring("Bearer ".length());
        return AuthService.getInstance().isVerified(jwtToken) && AuthService.getInstance().isVoter(jwtToken);
    }

}
