/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.entity.FeeTypePK;
import com.kasneb.entity.KasnebCourse;
import com.kasneb.entity.Paper;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.io.IOException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("fee")
@Stateless
public class FeeTypeRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Hibernate5Module hbm = new Hibernate5Module();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;

    @EJB
    com.kasneb.session.FeeFacade feeFacade;

    /**
     * Creates a new instance of FeeTypeRest
     */
    public FeeTypeRest() {
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    private FeeTypePK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;studentId=studentIdValue;courseId=courseIdValue'.
         * Here 'somePath' is a result of 
        () method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        com.kasneb.entity.FeeTypePK key = new com.kasneb.entity.FeeTypePK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();

        java.util.List<String> feeTypeCode = map.get("feeTypeCode");
        if (feeTypeCode != null && !feeTypeCode.isEmpty()) {
            key.setFeeCode(feeTypeCode.get(0));
        }

        java.util.List<String> feeCode = map.get("feeCode");
        if (feeCode != null && !feeCode.isEmpty()) {
            key.setCode(feeCode.get(0));
        }
        return key;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        anyResponse = feeFacade.findAll();
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("registration")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRegistrationFee() throws JsonProcessingException {
        try {
            anyResponse = feeFacade.getCourseRegistrationFee(new KasnebCourse("01"));
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException e) {
            anyResponse = new CustomMessage(e.getStatusCode().getStatusCode(), e.getMessage());
            httpStatus = e.getStatusCode();
        } catch (IOException ex) {
            // Logger.getLogger(FeeTypeRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("registration/renewal")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRenewalFee() throws JsonProcessingException {
        try {
            anyResponse = feeFacade.getAnnualRegistrationRenewalFee(new KasnebCourse("02"), 2017);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException e) {
            anyResponse = new CustomMessage(e.getStatusCode().getStatusCode(), e.getMessage());
            httpStatus = e.getStatusCode();
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("examentry/paper")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findExamEntryFee() throws JsonProcessingException {
        try {
            anyResponse = feeFacade.getExamEntryFeePerPaper(new Paper("CA11"), true);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException e) {
            anyResponse = new CustomMessage(e.getStatusCode().getStatusCode(), e.getMessage());
            httpStatus = e.getStatusCode();
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
}
