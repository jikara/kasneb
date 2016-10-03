/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.QualificationType;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
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
    @EJB
    com.kasneb.session.QualificationTypeFacade qualificationTypeFacade;

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
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("type_id") Integer typeId) throws JsonProcessingException {
        try {
            if (typeId != null) {
                QualificationType type = qualificationTypeFacade.find(typeId);
                if (type == null) {
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Qualification type does not exist");
                }
                anyResponse = qualificationFacade.findByType(type);
            } else {
                anyResponse = qualificationFacade.findAll();
            }
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of QualificationRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
