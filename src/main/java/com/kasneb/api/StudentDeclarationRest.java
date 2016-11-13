/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
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
@Path("studentdeclaration")
public class StudentDeclarationRest {

    @Context
    private UriInfo context;
    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.StudentDeclarationFacade studentDeclarationFacade;

    /**
     * Creates a new instance of StudentDeclarationRest
     */
    public StudentDeclarationRest() {
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.StudentDeclarationRest
     *
     * @param id
     * @param response
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("id") Integer id, @QueryParam("response") Boolean response) {
        try {
            if (id != null && response != null) {
                anyResponse = studentDeclarationFacade.findAll(id, response);
            } else if (id != null && response == null) {
                anyResponse = studentDeclarationFacade.findAll(id);
            } else if (id == null && response != null) {
                anyResponse = studentDeclarationFacade.findAll(response);
            } else {
                anyResponse = studentDeclarationFacade.findAll();
            }
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DeclarationRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of StudentDeclarationRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
