/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("feecode")
public class FeeCodeRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;

    @EJB
    com.kasneb.session.FeeCodeFacade feeCodeFacade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJson() {
        try {
            anyResponse = feeCodeFacade.findAll();
            httpStatus = Response.Status.OK;
        } catch (Exception e) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
           // Logger.getLogger(FeeTypeRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
}
