/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("qualification")
public class QualificationRest {

    @Context
    private UriInfo context;
    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.QualificationFacade qualificationFacade;

    /**
     * Creates a new instance of QualificationRest
     */
    public QualificationRest() {
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.QualificationRest
     *
     * @param typeId
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("type_id") Integer typeId) {
        try {
            anyResponse = qualificationFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
           // Logger.getLogger(QualificationRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }
}
