/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import com.kasneb.util.PredicateUtil;
import java.io.IOException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
@Stateless
public class ExamCentreRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Hibernate5Module hbm = new Hibernate5Module();
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
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.ExamCentreRest
     *
     * @param studentId_
     * @param zoneCode
     * @return an instance of Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("studentId") String studentId_, @QueryParam("zoneCode") String zoneCode) throws JsonProcessingException {
        StudentCourse currentCourse = null;
        try {
            if (PredicateUtil.isSet(studentId_) && PredicateUtil.isSet(zoneCode)) {
                Integer studentId=Integer.parseInt(studentId_);
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
        } catch (IOException | NumberFormatException ex) {
            anyResponse = new CustomMessage(500, ex.getMessage());
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            // Logger.getLogger(ExamCentreRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

}
