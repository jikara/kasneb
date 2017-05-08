/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.kasneb.dto.ExemptionData;
import com.kasneb.entity.Course;
import com.kasneb.entity.CourseExemption;
import com.kasneb.entity.pk.CourseExemptionPK;
import com.kasneb.exception.CustomMessage;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
@Path("courseexemption")
@Stateless
public class CourseExemptionRest {

    @Context
    private UriInfo context;
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.CourseFacade courseFacade;
    @EJB
    com.kasneb.session.CourseExemptionFacade courseExemptionFacade;

    /**
     * Creates a new instance of CourseExemptionRest
     */
    public CourseExemptionRest() {
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.CourseExemptionRest
     *
     * @param id
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") String id) {
        Course course = courseFacade.find(id);
        anyResponse = courseExemptionFacade.findByQualification(course);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of CourseExemptionRest
     *
     * @param entity
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response edit(CourseExemption entity) {
        entity = courseExemptionFacade.edit(entity);
        anyResponse = entity;
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ExemptionData entity) {
        CourseExemptionPK pk = new CourseExemptionPK(entity.getQualification().getId(), entity.getCourse().getId(), entity.getPaper().getCode());
        CourseExemption courseExemption = new CourseExemption(pk);
        courseExemptionFacade.create(courseExemption);
        anyResponse = courseExemption;
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(courseExemption)
                .build();
    }

    @DELETE
    @Path("{qualificationId}/{paperCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("qualificationId") String qualificationId, @PathParam("paperCode") String paperCode) {
        CourseExemption courseExemption = courseFacade.findCourseExemption(qualificationId, paperCode);
        courseExemptionFacade.remove(courseExemption);
        anyResponse = new CustomMessage(200, "Record removed successfully");
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

}
