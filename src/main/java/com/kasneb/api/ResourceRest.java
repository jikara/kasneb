/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("resources")
public class ResourceRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.ExemptionFacade exemptionFacade;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.StudentCourseSittingFacade studentCourseSittingFacade;

    /**
     * Creates a new instance of ResourcesRest
     */
    public ResourceRest() {
    }

    @GET
    @Path("exemption_letter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExemptionLetters(@QueryParam("studentId") Integer studentId) throws JsonProcessingException {
        StudentCourse studentCourse;
        try {
            Student student = studentFacade.findStudent(studentId);
            if (student == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not exist");
            }
            studentCourse = student.getCurrentCourse();
            if (studentCourse == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not have an active course");
            }
            anyResponse = exemptionFacade.findByStudentCourse(studentCourse);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
           // Logger.getLogger(UserRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("timetable")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTimetables(@QueryParam("studentId") Integer studentId) throws JsonProcessingException {
        StudentCourse studentCourse;
        try {
            Student student = studentFacade.findStudent(studentId);
            if (student == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not exist");
            }
            studentCourse = student.getCurrentCourse();
            if (studentCourse == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student does not have an active course");
            }
            anyResponse = studentCourseSittingFacade.findByStudentCourse(studentCourse);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
           // Logger.getLogger(UserRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
}
