/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomHttpException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("centrezone")
public class CentreZoneRest {

    @Context
    private UriInfo context;

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.CentreZoneFacade centreZoneFacade;
    @EJB
    private com.kasneb.session.StudentFacade studentFacade;

    /**
     * Creates a new instance of CentrezoneResource
     */
    public CentreZoneRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.CentreZoneRest
     *
     * @param studentId
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("studentId") Integer studentId) {
        StudentCourse currentCourse = null;
        try {
            if (studentId != null) {
                Student student = studentFacade.find(studentId);
                if (student != null) {
                    currentCourse = student.getCurrentCourse();
                }
                anyResponse = centreZoneFacade.findZones(currentCourse);
            } else {
                anyResponse = centreZoneFacade.findZones();
            }
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
           // Logger.getLogger(CentreZoneRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | CustomHttpException ex) {
           // Logger.getLogger(CentreZoneRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }
}
