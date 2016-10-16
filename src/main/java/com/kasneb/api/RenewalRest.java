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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("renewal")
public class RenewalRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;

    /**
     * Creates a new instance of RenewalRest
     */
    public RenewalRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.RenewalRest
     *
     * @param id
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Path("invoice/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRenewalInvoice(@PathParam("id") Integer id) {
        Student student = studentFacade.find(id);
        try {
            if (student == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not exist");
            }
            if (student.getCurrentCourse() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student has no active course");
            }
            if (new Date().after(student.getCurrentCourse().getCurrentSubscription().getExpiry())) {
                //Genereta invoice
                anyResponse = studentCourseFacade.generateRenewalInvoice(student.getCurrentCourse());
            } else {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Current Subscription is still active");
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException e) {
            anyResponse = new CustomMessage(e.getStatusCode().getStatusCode(), e.getMessage());
            httpStatus = e.getStatusCode();
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(RenewalRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of RenewalRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
