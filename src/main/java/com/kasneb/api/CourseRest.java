/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.Institution;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("course")
public class CourseRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.CourseFacade courseFacade;
    @EJB
    com.kasneb.session.KasnebCourseFacade kasnebCourseFacade;
    @EJB
    com.kasneb.session.OtherCourseFacade otherCourseFacade;
    @EJB
    com.kasneb.session.InstitutionFacade institutionFacade;

    /**
     * Creates a new instance of CourseRest
     */
    public CourseRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.CourseRest
     *
     * @return an instance of Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            anyResponse = courseFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(CourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("kasneb")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findKasneb() {
        try {
            anyResponse = kasnebCourseFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(CourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("other")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOther() {
        try {
            anyResponse = otherCourseFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(CourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("other/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOtherByInstitution(@PathParam("id") Integer institutionId) {
        try {
            Institution institution = institutionFacade.find(institutionId);
            if (institution == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Institution does not exist");
            }
            anyResponse = otherCourseFacade.findByInstitution(institution);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(CourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
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
            anyResponse = courseFacade.find(id);
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(CourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     *
     * @param code
     * @return
     */
    @GET
    @Path("type/{code}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByType(@PathParam("code") Integer code) {
        try {
            anyResponse = kasnebCourseFacade.findByType(code);
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(CourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("exemption/type/{code}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findQualificationByType(@PathParam("code") Integer code, @QueryParam("student") Integer regionId) {
        try {
            anyResponse = kasnebCourseFacade.findByType(code);
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(CourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of CourseRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
