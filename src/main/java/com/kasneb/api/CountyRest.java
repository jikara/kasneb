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
@Path("county")
public class CountyRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.CountyFacade countyFacade;

    /**
     * Creates a new instance of CountyRest
     */
    public CountyRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.CountyRest
     *
     * @return an instance of Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            anyResponse = countyFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
           // Logger.getLogger(CountyRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }
}
