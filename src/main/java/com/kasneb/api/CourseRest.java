/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("course")
public class CourseRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.CourseFacade courseFacade;

    /**
     * Creates a new instance of CourseRest
     */
    public CourseRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.CourseRest
     *
     * @return an instance of Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        anyResponse = courseFacade.findAll();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     *
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id) throws JsonProcessingException {
        anyResponse = courseFacade.find(id);
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     *
     * @param code
     * @return
     * @throws JsonProcessingException
     */
    @GET
    @Path("type/{code}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByType(@PathParam("code") Integer code) throws JsonProcessingException {
        anyResponse = courseFacade.findByType(code);
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of CourseRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
