/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.KasnebCourseType;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("coursetype")
@Stateless
public class CourseTypeRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Hibernate5Module hbm = new Hibernate5Module();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.CourseTypeFacade courseTypeFacade;
    @EJB
    com.kasneb.session.KasnebCourseTypeFacade kasnebCourseTypeFacade;
    @EJB
    com.kasneb.session.OtherCourseTypeFacade otherCourseTypeFacade;

    /**
     * Creates a new instance of CoursetypeRest
     */
    public CourseTypeRest() {
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.CourseTypeRest
     *
     * @return an instance of Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() throws JsonProcessingException {
        List<KasnebCourseType> courseTypes = kasnebCourseTypeFacade.findAll();
        for (KasnebCourseType kasnebCourseType : courseTypes) {
            Collection<KasnebCourse> courses=kasnebCourseType.getCourseCollection();
            kasnebCourseType.setCourseCollection(courses);
            
        }
        json = mapper.writeValueAsString(courseTypes);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("other")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOther() {
        try {
            anyResponse = otherCourseTypeFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            // Logger.getLogger(CourseTypeRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@PathParam("code") Integer code) {
        try {
            anyResponse = courseTypeFacade.find(code);
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            // Logger.getLogger(CourseTypeRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }
}
