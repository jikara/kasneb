/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.entity.Institution;
import com.kasneb.entity.OtherCourseType;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import com.kasneb.util.PredicateUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("institution")
@Stateless
public class InstitutionRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Hibernate5Module hbm = new Hibernate5Module();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.InstitutionFacade institutionFacade;
    @EJB
    com.kasneb.session.OtherCourseTypeFacade otherCourseTypeFacade;

    /**
     * Creates a new instance of InstitutionRest
     */
    public InstitutionRest() {
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.InstitutionRest
     *
     * @param courseTypeId_
     * @return an instance of javax.ws.rs.core.Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("courseTypeId") String courseTypeId_) throws JsonProcessingException {
        Collection<Institution> institutions = new ArrayList<>();
        try {
            if (PredicateUtil.isSet(courseTypeId_)) {
                Integer courseTypeId = Integer.parseInt(courseTypeId_);
                OtherCourseType courseType = otherCourseTypeFacade.find(courseTypeId);
                if (courseType == null) {
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Other course type does not exist");
                }
                institutions = institutionFacade.findByCourseType(courseType);
            } else {
                institutions = institutionFacade.findAll();
            }
            httpStatus = Response.Status.OK;
            for (Institution institution : institutions) {
                institution.getCourses().size();
            }
            anyResponse = institutions;
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Institution entity) {
        institutionFacade.create(entity);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        Institution institution = institutionFacade.find(id);
        institutionFacade.remove(institution);
        anyResponse = new CustomMessage(200, "Record removed successfully");
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }
}
