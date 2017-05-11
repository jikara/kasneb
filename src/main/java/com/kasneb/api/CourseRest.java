/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
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
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
@Stateless
public class CourseRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Hibernate5Module hbm = new Hibernate5Module();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
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
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.CourseRest
     *
     * @return an instance of Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Path("kasneb")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response findKasneb() throws JsonProcessingException {
        Collection<KasnebCourse> courses = kasnebCourseFacade.findKasnebCourses();
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(courses);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("other")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findOther(@QueryParam("institutionId") Integer institutionId) throws JsonProcessingException {
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
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
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
    public Response find(@PathParam("id") String id) throws JsonProcessingException {
        anyResponse = courseFacade.find(id);
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
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
    public Response findByType(@PathParam("code") Integer code) throws JsonProcessingException {
        anyResponse = kasnebCourseFacade.findByType(code);
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("exemption/type/{code}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findQualificationByType(@PathParam("code") Integer code, @QueryParam("student") Integer regionId) throws JsonProcessingException {
        anyResponse = kasnebCourseFacade.findByType(code);
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Path("exemption/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(CourseExemption entity, @PathParam("id") String id) throws IllegalAccessException, InvocationTargetException, JsonProcessingException {
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
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        anyResponse = courseFacade.findAll();
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Course entity) throws JsonProcessingException {
        String maxId = courseFacade.getMaximun();
        OtherCourse otherCourse = new OtherCourse();
        otherCourse.setId(maxId);
        otherCourse.setName(entity.getName());
        otherCourse.setInstitution(entity.getInstitution());
        Institution institution = institutionFacade.find(entity.getInstitution().getId());
        otherCourse.setCourseType(institution.getCourseType());
        courseFacade.create(otherCourse);
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(otherCourse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) throws JsonProcessingException {
        Course course = courseFacade.find(id);
        courseFacade.remove(course);
        anyResponse = new CustomMessage(200, "Record removed successfully");
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

}
