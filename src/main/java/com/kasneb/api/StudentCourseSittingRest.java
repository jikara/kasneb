/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseSitting;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;
import javax.ejb.EJB;
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
public class StudentCourseSittingRest {

    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    @EJB
    com.kasneb.session.StudentCourseSittingFacade studentCourseSittingFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;

    /**
     * Creates a new instance of StudentsittingRest
     */
    public StudentCourseSittingRest() {
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Integer id) {
        StudentCourseSitting studentCourseSitting = studentCourseSittingFacade.find(id);
        return Response
                .status(Response.Status.OK)
                .entity(studentCourseSitting)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<StudentCourseSitting> studentCourseSittings = studentCourseSittingFacade.findAll();
        return Response
                .status(Response.Status.OK)
                .entity(studentCourseSittings)
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
        List<StudentCourseSitting> studentCourseSittings = studentCourseSittingFacade.findAll();
        return Response
                .status(Response.Status.OK)
                .entity(studentCourseSittings)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(StudentCourseSitting entity) {
        try {
            entity = studentCourseSittingFacade.createStudentCourseSitting(entity);
            httpStatus = Response.Status.OK;
        } catch (IllegalAccessException | InvocationTargetException ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            // Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            // Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of
     * StudentCourseSittingRest
     *
     * @param entity
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response edit(StudentCourseSitting entity) {
        try {
            entity = studentCourseSittingFacade.update(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
        } catch (IllegalAccessException | InvocationTargetException | IOException | ParseException ex) {
            // Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
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
            entity = studentCourseSittingFacade.updateCentre(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
        } catch (ParseException ex) {
            // Logger.getLogger(StudentCourseSittingRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }
}
