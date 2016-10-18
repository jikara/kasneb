/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("examcentre")
public class ExamCentreRest {

    @Context
    private UriInfo context;
    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    private com.kasneb.session.ExamCentreFacade examCentreFacade;

    /**
     * Creates a new instance of ExamCentreRest
     */
    public ExamCentreRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.ExamCentreRest
     *
     * @param courseId
     * @return an instance of Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("course_id") Integer courseId) {
        try {
            if (courseId == null) {
                anyResponse = examCentreFacade.findAll();
            } else {
                anyResponse = examCentreFacade.findByCourse(courseId);
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            anyResponse = new CustomMessage(500, ex.getMessage());
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("cluster/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByCluster(@QueryParam("course_id") Integer courseId, @QueryParam("cluster_id") Integer clusterId) {
        try {
            if (courseId == null) {
                anyResponse = examCentreFacade.findByCluster(clusterId);
            } else {
                anyResponse = examCentreFacade.findByCourseAndCluster(courseId, clusterId);
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("zone/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByZone(@QueryParam("course_id") Integer courseId, @QueryParam("zone_id") Integer zoneId) {
        try {
            if (courseId == null) {
                anyResponse = examCentreFacade.findByZone(zoneId);
            } else {
                anyResponse = examCentreFacade.findByCourseAndZone(courseId, zoneId);
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("region/filter")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByRegion(@QueryParam("course_id") Integer courseId, @QueryParam("region_id") Integer regionId) {
        try {
            if (courseId == null) {
                anyResponse = examCentreFacade.findByRegion(regionId);
            } else {
                anyResponse = examCentreFacade.findByCourseAndRegion(courseId, regionId);
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of ExamCentreRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
