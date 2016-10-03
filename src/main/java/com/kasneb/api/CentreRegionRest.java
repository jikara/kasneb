/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("centreregion")
public class CentreRegionRest {

    @Context
    private UriInfo context;

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.CentreRegionFacade centreRegionFacade;

    /**
     * Creates a new instance of CentreRegionRest
     */
    public CentreRegionRest() {
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.CentreRegionRest
     *
     * @return an instance of javax.ws.rs.core.Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        anyResponse = centreRegionFacade.findAll();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByRegion(@QueryParam("zone_id") Integer zoneId) throws JsonProcessingException {
        try {
            anyResponse = centreRegionFacade.findByZone(zoneId);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of CentreRegionRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
