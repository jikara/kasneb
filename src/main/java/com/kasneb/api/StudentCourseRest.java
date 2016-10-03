/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.Paper;
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomMessage;
import com.kasneb.exception.CustomHttpException;
import java.util.Collection;
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
@Path("studentcourse")
public class StudentCourseRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;

    /**
     * Creates a new instance of StudentCourseRest
     */
    public StudentCourseRest() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        anyResponse = studentCourseFacade.findAll();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.StudentCourseRest
     *
     * @return an instance of Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Path("pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPending() throws JsonProcessingException {
        anyResponse = studentCourseFacade.findPending();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("verified")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findVerified() throws JsonProcessingException {
        anyResponse = studentCourseFacade.findVerified();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("active/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findActive(@PathParam("id") Integer id) throws JsonProcessingException {
        Collection<Paper> papers = null;
        try {
            StudentCourse studentCourse = studentCourseFacade.findActive(id);
            if (studentCourse == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student has no active course");
            }
            //get part papers
            if (studentCourse.getCourse().getCourseType() == 100) {
                studentCourse.setEligiblePart(studentCourseFacade.getElligiblePart(studentCourse));
            } else if (studentCourse.getCourse().getCourseType() == 200) {
                studentCourse.setEligibleLevel(studentCourseFacade.getElligibleLevel(studentCourse));
            }
            studentCourse.setExemptedPapers(studentCourseFacade.getExemptedPapers(studentCourse));
            anyResponse = studentCourse;
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Integer id) throws JsonProcessingException {
        Collection<Paper> papers = null;
        try {
            StudentCourse studentCourse = studentCourseFacade.find(id);
            if (studentCourse == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student course does not exist");
            }
            //get part papers
            if (studentCourse.getCourse().getCourseType() == 100) {
                studentCourse.setEligiblePart(studentCourseFacade.getElligiblePart(studentCourse));
            } else if (studentCourse.getCourse().getCourseType() == 200) {
                studentCourse.setEligibleLevel(studentCourseFacade.getElligibleLevel(studentCourse));
            }
            httpStatus = Response.Status.OK;
            anyResponse = studentCourse;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     *
     * @param entity
     * @return
     * @throws JsonProcessingException
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(StudentCourse entity) throws JsonProcessingException {
        try {
            if (entity.getStudent() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student id is required");
            }
            if (entity.getCourse() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course id is required");
            }
            studentCourseFacade.createStudentCourse(entity);
            anyResponse=null;
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(StudentCourse entity) throws JsonProcessingException {
        try {
            studentCourseFacade.updateStudentCourse(entity);
            anyResponse = studentCourseFacade.find(entity.getId());
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Path("exemption")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response exemption(StudentCourse entity) throws JsonProcessingException {
        try {
            studentCourseFacade.createExemption(entity);
            anyResponse = studentCourseFacade.find(entity.getId());
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            httpStatus = Response.Status.OK;
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Path("verify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response verify(StudentCourse entity) throws JsonProcessingException {
        try {
            anyResponse = studentCourseFacade.verifyStudentCourse(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of StudentCourseRest
     *
     * @param entity
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
}
