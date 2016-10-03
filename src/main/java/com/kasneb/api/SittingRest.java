/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("sitting")
public class SittingRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.SittingFacade sittingFacade;

    /**
     * Creates a new instance of SittingRest
     */
    public SittingRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.SittingRest
     *
     * @return an instance of Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        anyResponse = sittingFacade.findAll();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findEligible() throws JsonProcessingException {
        anyResponse = sittingFacade.findEligible();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of SittingRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
