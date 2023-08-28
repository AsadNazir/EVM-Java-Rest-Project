package com.example.evm_2.resources;

import com.example.evm_2.domain.Party;
import com.example.evm_2.services.AuthService;
import com.example.evm_2.services.PartyService;
import com.example.evm_2.services.Scheduler;
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
    public Response startVoting(@HeaderParam("Authorization") String authorizationHeader) {
        try {
            if (!isAdmin(authorizationHeader)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            Scheduler scheduler = new Scheduler();
            scheduler.startTheVoting();
            return Response.status(200).entity(objectMapper.writeValueAsString(new CustomResponse(false, "Voting has been started"))).build();
        } catch (Exception E) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error Occurred Voting cannot be started").build();
        }
    }


    private boolean isAdmin(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//            return Response.status(Response.Status.UNAUTHORIZED).build(); // No or invalid authorization token
            return false;
        }


        String jwtToken = authorizationHeader.substring("Bearer ".length());

        //            return Response.status(Response.Status.UNAUTHORIZED).entity(new CustomResponse(true, "Unauthorized")).build(); // Token verification failed
        return AuthService.getInstance().isVerified(jwtToken) && AuthService.getInstance().isAdmin(jwtToken);
    }

}
