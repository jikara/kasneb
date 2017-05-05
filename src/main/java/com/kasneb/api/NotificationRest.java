/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.Student;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("notification")
public class NotificationRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.NotificationFacade notificationFacade;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;

    /**
     * Creates a new instance of NotificationRest
     */
    public NotificationRest() {
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.NotificationRest
     *
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            anyResponse = notificationFacade.findAll();
            httpStatus = Response.Status.OK;
        } catch (Exception ex) {
            anyResponse = new CustomMessage(500, ex.getMessage());
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
           // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
           // Logger.getLogger(InstitutionRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of NotificationRest
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id) {
        try {
            anyResponse = notificationFacade.find(id);
            httpStatus = Response.Status.OK;
        } catch (Exception ex) {
            anyResponse = new CustomMessage(500, ex.getMessage());
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
           // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
           // Logger.getLogger(InstitutionRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("student/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByStudent(@PathParam("id") Integer id) {
        try {
            Student student = studentFacade.find(id);
            if (student == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not exist");
            }
            anyResponse = notificationFacade.findByStudent(student);
            httpStatus = Response.Status.OK;
        } catch (Exception ex) {
            anyResponse = new CustomMessage(500, ex.getMessage());
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
           // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
           // Logger.getLogger(InstitutionRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
}
