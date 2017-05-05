/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.kasneb.entity.Institution;
import com.kasneb.entity.OtherCourseType;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
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
public class InstitutionRest {

    @Context
    private UriInfo context;
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    @EJB
    com.kasneb.session.InstitutionFacade institutionFacade;
    @EJB
    com.kasneb.session.OtherCourseTypeFacade otherCourseTypeFacade;

    /**
     * Creates a new instance of InstitutionRest
     */
    public InstitutionRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.InstitutionRest
     *
     * @param courseTypeId
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("courseTypeId") Integer courseTypeId) {
        try {
            if (courseTypeId == null) {
                anyResponse = institutionFacade.findAll();
            } else {
                OtherCourseType courseType = otherCourseTypeFacade.find(courseTypeId);
                if (courseType == null) {
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Other course type does not exist");
                }
                anyResponse = institutionFacade.findByCourseType(courseType);
            }
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
