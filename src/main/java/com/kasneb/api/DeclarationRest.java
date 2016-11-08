/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.StudentDeclaration;
import com.kasneb.exception.CustomMessage;
import com.kasneb.exception.CustomHttpException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("declaration")
public class DeclarationRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.DeclarationFacade declarationFacade;

    /**
     * Creates a new instance of StudentRest
     */
    public DeclarationRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.StudentRest
     *
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            anyResponse = declarationFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DeclarationRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id) {
        try {
            anyResponse = declarationFacade.find(id);
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

    @GET
    @Path("{id}/{response}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id, @PathParam("response") Boolean response) {
        try {
            anyResponse = declarationFacade.findByResponse(id,response);
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

//    @POST
//    @Path("register")
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response register(StudentDeclaration studentDeclaration) throws JsonProcessingException {
//        try {
//            declarationFacade.register(studentDeclaration);
//            anyResponse = "Declaration  registered successfully";
//            httpStatus = Response.Status.OK;
//        } catch (CustomHttpException ex) {
//            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
//            httpStatus = ex.getStatusCode();
//            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        json = mapper.writeValueAsString(anyResponse);
//        return Response
//                .status(httpStatus)
//                .entity(json)
//                .build();
//    }
    /**
     *
     * @param studentDeclaration
     * @return
     */
    @PUT
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(StudentDeclaration studentDeclaration) {
        try {
            declarationFacade.update(studentDeclaration);
            anyResponse = "Declaration  updated successfully";
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(DeclarationRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

}
