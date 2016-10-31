/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.StudentCourseSitting;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("studentcoursesitting")
public class StudentCourseSittingRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.StudentCourseSittingFacade studentCourseSittingFacade;

    /**
     * Creates a new instance of StudentsittingRest
     */
    public StudentCourseSittingRest() {
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Integer id) {
        try {
            anyResponse = studentCourseSittingFacade.find(id);
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            anyResponse = studentCourseSittingFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.StudentCourseSittingRest
     *
     * @return an instance of Response
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findList() {
        try {
            anyResponse = studentCourseSittingFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(StudentCourseSitting entity) throws JsonProcessingException {
        try {
            studentCourseSittingFacade.createStudentCourse(entity);
            anyResponse = studentCourseSittingFacade.find(entity.getId());
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of
     * StudentCourseSittingRest
     *
     * @param entity
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response edit(StudentCourseSitting entity) throws JsonProcessingException {
        try {
            studentCourseSittingFacade.update(entity);
            anyResponse = studentCourseSittingFacade.find(entity.getId());
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Path("centre")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setCentre(StudentCourseSitting entity) throws JsonProcessingException {
        try {
            if (entity.getId() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Id is not defined");
            }
            if (entity.getId() == 0) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Id cannot be zero");
            }
            studentCourseSittingFacade.updateCentre(entity);
            anyResponse = studentCourseSittingFacade.find(entity.getId());
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
}
