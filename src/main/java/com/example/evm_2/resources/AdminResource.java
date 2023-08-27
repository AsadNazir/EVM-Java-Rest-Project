package com.example.evm_2.resources;

import com.example.evm_2.domain.Party;
import com.example.evm_2.services.AuthService;
import com.example.evm_2.services.PartyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.Part;
import jakarta.ws.rs.*;
import com.example.evm_2.commons.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/admin")
public class AdminResource {
    ObjectMapper objectMapper = new ObjectMapper();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/addParty")
    public String addParty(@HeaderParam("Authorization") String authorizationHeader, String party) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return "Unauthorized"; // No or invalid authorization token
            }

            String jwtToken = authorizationHeader.substring("Bearer ".length());

            if (!AuthService.getInstance().isVerified(jwtToken)) {
                return "Unauthorized"; // Token verification failed
            }

            Party P = objectMapper.readValue(party, Party.class);
            return objectMapper.writeValueAsString(PartyService.getInstance().addParty(P));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllParties")
    public String getAllParties() {
        try {
            List<Party> L = PartyService.getInstance().getAllParties();

            return objectMapper.writeValueAsString(new Response(false, L));

        } catch (Exception E) {

            E.printStackTrace();
        }

        return null;
    }


}
