/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.kasneb.dto.CourseExemption;
import com.kasneb.entity.Course;
import com.kasneb.entity.Institution;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.OtherCourse;
import com.kasneb.entity.Paper;
import com.kasneb.entity.pk.CourseExemptionPK;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
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

    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
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
    @Path("kasneb")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findKasneb() {
        anyResponse = kasnebCourseFacade.findKasnebCourses();
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

    @GET
    @Path("other")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOther(@QueryParam("institutionId") Integer institutionId) {
        try {
            if (institutionId == null) {
                anyResponse = otherCourseFacade.findAll();
            } else {
                Institution institution = institutionFacade.find(institutionId);
                if (institution == null) {
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Institution does not exist");
                }
                anyResponse = otherCourseFacade.findByInstitution(institution);
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(CourseRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(anyResponse)
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
    public Response find(@PathParam("id") String id) {
        anyResponse = courseFacade.find(id);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(anyResponse)
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
        anyResponse = kasnebCourseFacade.findByType(code);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

    @GET
    @Path("exemption/type/{code}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findQualificationByType(@PathParam("code") Integer code, @QueryParam("student") Integer regionId) {
        anyResponse = kasnebCourseFacade.findByType(code);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

    @PUT
    @Path("exemption/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(CourseExemption entity, @PathParam("id") String id) throws IllegalAccessException, InvocationTargetException {
        try {
            List<com.kasneb.entity.CourseExemption> courseExemptions = new ArrayList<>();
            Course managed = courseFacade.find(id);
            KasnebCourse kasnebCourse = kasnebCourseFacade.find(entity.getCourseCode());
            if (kasnebCourse == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Kasneb examination does not exist");
            }
            if (managed == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Qualification does not exist");
            }
            for (Paper paper : entity.getPapers()) {
                courseExemptions.add(new com.kasneb.entity.CourseExemption(new CourseExemptionPK(id, entity.getCourseCode(), paper.getCode())));
            }
            managed.getCourseExemptions().addAll(courseExemptions); //Add exemptions
            courseFacade.edit(managed);
            httpStatus = Response.Status.OK;
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), "Exmeption data successfully added");;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
        }
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        anyResponse = courseFacade.findAll();
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Course entity) {
        String maxId = courseFacade.getMaximun();
        OtherCourse otherCourse = new OtherCourse();
        otherCourse.setId(maxId);
        otherCourse.setName(entity.getName());
        otherCourse.setInstitution(entity.getInstitution());
        Institution institution = institutionFacade.find(entity.getInstitution().getId());
        otherCourse.setCourseType(institution.getCourseType());
        courseFacade.create(otherCourse);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(otherCourse)
                .build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
            Course course = courseFacade.find(id);
            courseFacade.remove(course);
            anyResponse = new CustomMessage(200, "Record removed successfully");
            httpStatus = Response.Status.OK;        
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

}
