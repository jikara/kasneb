/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
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
    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Hibernate5Module hbm = new Hibernate5Module();
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
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.CourseExemptionRest
     *
     * @param id
     * @return an instance of javax.ws.rs.core.Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") String id) throws JsonProcessingException {
        Course course = courseFacade.find(id);
        anyResponse = courseExemptionFacade.findByQualification(course);
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
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
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response edit(CourseExemption entity) throws JsonProcessingException {
        entity = courseExemptionFacade.edit(entity);
        anyResponse = entity;
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(ExemptionData entity) throws JsonProcessingException {
        CourseExemptionPK pk = new CourseExemptionPK(entity.getQualification().getId(), entity.getCourse().getId(), entity.getPaper().getCode());
        CourseExemption courseExemption = new CourseExemption(pk);
        courseExemptionFacade.create(courseExemption);
        anyResponse = courseExemption;
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @DELETE
    @Path("{qualificationId}/{paperCode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("qualificationId") String qualificationId, @PathParam("paperCode") String paperCode) throws JsonProcessingException {
        CourseExemption courseExemption = courseFacade.findCourseExemption(qualificationId, paperCode);
        courseExemptionFacade.remove(courseExemption);
        anyResponse = new CustomMessage(200, "Record removed successfully");
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

}
