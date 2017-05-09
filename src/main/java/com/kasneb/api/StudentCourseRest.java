/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.entity.Exemption;
import com.kasneb.entity.Paper;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.StudentCourseStatus;
import com.kasneb.entity.Synchronization;
import com.kasneb.exception.CustomMessage;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.BatchStudentCourse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Collection;
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
@Path("studentcourse")
@Stateless
public class StudentCourseRest {

    Object anyResponse = new Object();
    String json;
    ObjectMapper mapper = new ObjectMapper();
    Hibernate5Module hbm = new Hibernate5Module();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;
    @EJB
    com.kasneb.session.ExemptionFacade exemptionFacade;
    @EJB
    com.kasneb.session.CourseFacade courseFacade;
    @EJB
    com.kasneb.session.OtherCourseFacade otherCourseFacade;
    @EJB
    com.kasneb.session.CommunicationFacade communicationFacade;
    @EJB
    com.kasneb.session.SynchronizationFacade synchronizationFacade;

    /**
     * Creates a new instance of StudentCourseRest
     */
    public StudentCourseRest() {
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        List<StudentCourse> studentCourses = studentCourseFacade.findAll();
        json = mapper.writeValueAsString(studentCourses);
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
        List<StudentCourse> studentCourses = studentCourseFacade.findPending();
        json = mapper.writeValueAsString(studentCourses);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("pending/identification")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPendingIdentification() throws JsonProcessingException {
        List<StudentCourse> studentCourses = studentCourseFacade.findPendingIdentification();
        json = mapper.writeValueAsString(studentCourses);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @PUT
    @Path("verify/identification")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verifyIdentification(StudentCourse entity) throws IllegalAccessException, InvocationTargetException, IOException, ParseException {
        StudentCourse managed = null;
        try {
            managed = studentCourseFacade.find(entity.getId());
            if (managed == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student course does not exist");
            }
            studentCourseFacade.copy(entity, managed);
            managed = studentCourseFacade.identify(managed);
            if (managed.getCourseStatus().equals(StudentCourseStatus.ACTIVE)) {
                Student student = managed.getStudent();
                student.setCurrentCourse(managed);
                Synchronization synchronization = new Synchronization(student, false);
                synchronizationFacade.create(synchronization);
            }
            anyResponse = entity;
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(managed);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("verified")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findVerified() throws JsonProcessingException {
        List<StudentCourse> studentCourses = studentCourseFacade.findVerified();
        json = mapper.writeValueAsString(studentCourses);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("active/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findActive(@PathParam("id") Integer id) throws JsonProcessingException {
        StudentCourse studentCourse = null;
        try {
            studentCourse = studentCourseFacade.find(id);
                    studentCourseFacade.getElligiblePart(id);
            int x = studentCourse.getElligiblePapers().size();
            if (studentCourse == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student has no active course");
            }
            if (!studentCourse.getActive()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student has no active course");
            }
            httpStatus = Response.Status.OK;
            anyResponse = studentCourse;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
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
            httpStatus = Response.Status.OK;
            anyResponse = studentCourse;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("eligible_exemptions/{studentCourseId}/{qualification_id}/{code_type}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getEligibleExemptions(@PathParam("studentCourseId") Integer studentCourseId, @PathParam("qualification_id") String qualificationId, @PathParam("code_type") Integer codeType) throws JsonProcessingException {
        try {
            anyResponse = studentCourseFacade.getEligibleExemptionsByQualification(studentCourseId, qualificationId, codeType);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @POST
    @Path("eligible_exemptions/{studentCourseId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getEligibleExemptions(@PathParam("studentCourseId") Integer studentCourseId, List<String> qualificationIds) throws JsonProcessingException {
        try {
            anyResponse = studentCourseFacade.getEligibleExemptions(studentCourseId, qualificationIds);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
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
            entity = studentCourseFacade.createStudentCourse(entity);
            anyResponse = entity;
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Path("documents")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateDocuments(StudentCourse entity) throws JsonProcessingException {
        try {
            anyResponse = studentCourseFacade.updateStudentCourseDocuments(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException | InstantiationException | IOException | InvocationTargetException ex) {
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
            anyResponse = studentCourseFacade.updateStudentCourse(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException | InstantiationException | IOException | InvocationTargetException ex) {
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Path("exemption/complete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response completeExemption(Exemption entity) throws JsonProcessingException {
        entity = exemptionFacade.edit(entity);
        anyResponse = entity;
        httpStatus = Response.Status.OK;
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
        String body;
        try {
            anyResponse = studentCourseFacade.verifyStudentCourse(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | MessagingException | ParseException | IllegalAccessException | InvocationTargetException ex) {
            // Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Path("verify/batch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response verifyBatch(BatchStudentCourse entity) throws JsonProcessingException {
        try {
            studentCourseFacade.verifyBatchStudentCourse(entity);
            anyResponse = new CustomMessage(200, "Batch verification successfully processed");
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | MessagingException | ParseException ex) {
            // Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
}
