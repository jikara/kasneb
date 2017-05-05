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
import com.kasneb.exception.CustomMessage;
import com.kasneb.util.PredicateUtil;
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
    @EJB
    private com.kasneb.session.StudentFacade studentFacade;

    /**
     * Creates a new instance of ExamCentreRest
     */
    public ExamCentreRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.ExamCentreRest
     *
     * @param studentId
     * @param zoneCode
     * @return an instance of Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("studentId") Integer studentId, @QueryParam("zoneCode") String zoneCode) {
        StudentCourse currentCourse = null;
        try {
            if (studentId != null && PredicateUtil.isSet(zoneCode)) {
                Student student = studentFacade.find(studentId);
                if (student != null) {
                    currentCourse = student.getCurrentCourse();
                }
                anyResponse = examCentreFacade.findByZone(currentCourse, zoneCode);
            } else {
                anyResponse = examCentreFacade.findCentres();
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
           // Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            anyResponse = new CustomMessage(500, ex.getMessage());
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
           // Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
           // Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

}
