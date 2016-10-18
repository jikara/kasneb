/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.Course;
import com.kasneb.entity.CourseExemption;
import com.kasneb.entity.Paper;
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomMessage;
import com.kasneb.exception.CustomHttpException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
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
    @EJB
    com.kasneb.session.CourseFacade courseFacade;

    /**
     * Creates a new instance of StudentCourseRest
     */
    public StudentCourseRest() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            anyResponse = studentCourseFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     */
    @GET
    @Path("pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPending() {
        try {
            anyResponse = studentCourseFacade.findPending();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("verified")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findVerified() {
        try {
            anyResponse = studentCourseFacade.findVerified();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("active/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findActive(@PathParam("id") Integer id) {
        Collection<Paper> papers = null;
        try {
            StudentCourse studentCourse = studentCourseFacade.findActive(id);
            if (studentCourse == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This student has no active course");
            }
            //get part papers
            if (studentCourse.getCourse().getCourseType().getCode() == 100) {
                studentCourse.setEligiblePart(studentCourseFacade.getElligiblePart(studentCourse));
            } else if (studentCourse.getCourse().getCourseType().getCode() == 200) {
                studentCourse.setEligibleLevel(studentCourseFacade.getElligibleLevel(studentCourse));
            }
            anyResponse = studentCourse;
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
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
            //get part papers
            if (studentCourse.getCourse().getCourseType().getCode() == 100) {
                studentCourse.setEligiblePart(studentCourseFacade.getElligiblePart(studentCourse));
            } else if (studentCourse.getCourse().getCourseType().getCode() == 200) {
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
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("eligible_exemptions/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getEligibleExemptions(@PathParam("id") Integer id) {
        Set<Paper> eligibleExemptions = new HashSet<>();
        try {
            Course qualification = courseFacade.find(id);
            if (qualification == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This qualification course does not exist");
            }
            for (CourseExemption c : qualification.getCourseExemptions()) {
                eligibleExemptions.add(c.getPaper());
            }
            anyResponse = eligibleExemptions;
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("eligible_exemptions")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getKasnebEligibleExemptions() {
        Set<Paper> eligibleExemptions = new HashSet<>();
        try {
            //Get previous courses            
            Course qualification = courseFacade.find(1);
            if (qualification == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "This qualification course does not exist");
            }
            for (CourseExemption c : qualification.getCourseExemptions()) {
                eligibleExemptions.add(c.getPaper());
            }
            anyResponse = eligibleExemptions;
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    public Response create(StudentCourse entity) {
        try {
            if (entity.getStudent() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student id is required");
            }
            if (entity.getCourse() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Course id is required");
            }
            entity = (StudentCourse) studentCourseFacade.createStudentCourse(entity);
            anyResponse = studentCourseFacade.find(entity.getId());
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response update(StudentCourse entity) {
        try {
            anyResponse = studentCourseFacade.updateStudentCourse(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Path("exemption/complete")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response completeExemption(StudentCourse entity) {
        try {
            studentCourseFacade.completeExemption(entity);
            anyResponse = studentCourseFacade.find(entity.getId());
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Path("verify")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response verify(StudentCourse entity) {
        try {
            anyResponse = studentCourseFacade.verifyStudentCourse(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentCourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
