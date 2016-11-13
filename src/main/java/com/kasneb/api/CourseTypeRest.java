/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.exception.CustomHttpException;
import java.io.IOException;
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
@Path("coursetype")
public class CourseTypeRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.CourseTypeFacade courseTypeFacade;
    @EJB
    com.kasneb.session.KasnebCourseTypeFacade kasnebCourseTypeFacade;
    @EJB
    com.kasneb.session.OtherCourseTypeFacade otherCourseTypeFacade;

    /**
     * Creates a new instance of CoursetypeRest
     */
    public CourseTypeRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.CourseTypeRest
     *
     * @return an instance of Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            anyResponse = kasnebCourseTypeFacade.findCourseTypes();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(CourseTypeRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CourseTypeRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomHttpException ex) {
            Logger.getLogger(CourseTypeRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("other")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOther() {
        try {
            anyResponse = otherCourseTypeFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(CourseTypeRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("code") Integer code) {
        try {
            anyResponse = courseTypeFacade.find(code);
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(CourseTypeRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of CourseTypeRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
