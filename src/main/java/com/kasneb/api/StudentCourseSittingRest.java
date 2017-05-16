/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.entity.StudentCourseSitting;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.MessagingException;
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
@Stateless
public class StudentCourseSittingRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Hibernate5Module hbm = new Hibernate5Module();
    String json;
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    @EJB
    com.kasneb.session.StudentCourseSittingFacade studentCourseSittingFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;

    /**
     * Creates a new instance of StudentsittingRest
     */
    public StudentCourseSittingRest() {
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Integer id) throws JsonProcessingException {
        StudentCourseSitting studentCourseSitting = studentCourseSittingFacade.find(id);
        json = mapper.writeValueAsString(studentCourseSitting);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        List<StudentCourseSitting> studentCourseSittings = studentCourseSittingFacade.findAll();
        json = mapper.writeValueAsString(studentCourseSittings);
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
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findList() throws JsonProcessingException {
        List<StudentCourseSitting> studentCourseSittings = studentCourseSittingFacade.findAll();
        json = mapper.writeValueAsString(studentCourseSittings);
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
            anyResponse = studentCourseSittingFacade.createStudentCourseSitting(entity);
            httpStatus = Response.Status.OK;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
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
            anyResponse = studentCourseSittingFacade.update(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
        } catch (IllegalAccessException | InvocationTargetException | IOException | ParseException ex) {
            Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
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
    public Response setCentre(StudentCourseSitting entity) throws MessagingException, IOException {
        try {
            if (entity.getId() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Id is not defined");
            }
            if (entity.getId() == 0) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Id cannot be zero");
            }
            anyResponse = studentCourseSittingFacade.updateCentre(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
        } catch (ParseException ex) {
            // Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
}
