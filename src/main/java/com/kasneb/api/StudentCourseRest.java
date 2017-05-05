/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

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
@Path("studentcourse")
public class StudentCourseRest {

    Object anyResponse = new Object();
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
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<StudentCourse> studentCourses = studentCourseFacade.findAll();
        return Response
                .status(Response.Status.OK)
                .entity(studentCourses)
                .build();
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.StudentCourseRest
     *
     * @return an instance of Response
     */
    @GET
    @Path("pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPending() {
        List<StudentCourse> studentCourses = studentCourseFacade.findPending();
        return Response
                .status(Response.Status.OK)
                .entity(studentCourses)
                .build();
    }

    @GET
    @Path("pending/identification")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPendingIdentification() {
        List<StudentCourse> studentCourses = studentCourseFacade.findPendingIdentification();
        return Response
                .status(Response.Status.OK)
                .entity(studentCourses)
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
        return Response
                .status(httpStatus)
                .entity(managed)
                .build();
    }

    @GET
    @Path("verified")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findVerified() {
        List<StudentCourse> studentCourses = studentCourseFacade.findVerified();
        return Response
                .status(Response.Status.OK)
                .entity(studentCourses)
                .build();
    }

    @GET
    @Path("active/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findActive(@PathParam("id") Integer id) {
        StudentCourse studentCourse = null;
        try {
            studentCourse = studentCourseFacade.findActive(id);
            if (studentCourse == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student has no active course");
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(studentCourse)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Integer id) {
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
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

    @GET
    @Path("eligible_exemptions/{studentCourseId}/{qualification_id}/{code_type}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getEligibleExemptions(@PathParam("studentCourseId") Integer studentCourseId, @PathParam("qualification_id") String qualificationId, @PathParam("code_type") Integer codeType) {
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
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

    @POST
    @Path("eligible_exemptions/{studentCourseId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getEligibleExemptions(@PathParam("studentCourseId") Integer studentCourseId, List<String> qualificationIds) {
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
        return Response
                .status(httpStatus)
                .entity(anyResponse)
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
    public Response create(StudentCourse entity) {
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
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

    @PUT
    @Path("documents")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response updateDocuments(StudentCourse entity) {
        try {
            anyResponse = studentCourseFacade.updateStudentCourseDocuments(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException | InstantiationException | IOException | InvocationTargetException ex) {
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(StudentCourse entity) {
        try {
            entity = studentCourseFacade.updateStudentCourse(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException | InstantiationException | IOException | InvocationTargetException ex) {
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @PUT
    @Path("exemption/complete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response completeExemption(Exemption entity) {
            entity=exemptionFacade.edit(entity);
            anyResponse = entity;
            httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @PUT
    @Path("verify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response verify(StudentCourse entity) {
        String body;
        try {
            entity = studentCourseFacade.verifyStudentCourse(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | MessagingException | ParseException | IllegalAccessException | InvocationTargetException ex) {
            // Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @PUT
    @Path("verify/batch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response verifyBatch(BatchStudentCourse entity) {
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
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }
}
