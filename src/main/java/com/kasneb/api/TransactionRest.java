/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.Student;
import com.kasneb.util.PredicateUtil;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
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
@Path("transaction")
public class TransactionRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.PaymentFacade paymentFacade;

    /**
     * Creates a new instance of TransactionRest
     */
    public TransactionRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.TransactionRest
     *
     * @return an instance of javax.ws.rs.core.Response
     */
    /**
     *
     * @param id
     * @param feeCode
     * @return
     */
    @GET
    @Path("student/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentTransactions(@PathParam("id") Integer id, @QueryParam("feeCode") String feeCode) {
        try {
            if (PredicateUtil.isSet(feeCode)) {
                anyResponse = paymentFacade.findAll(new Student(id), new FeeCode(feeCode));
            } else {
                anyResponse = paymentFacade.findAll(new Student(id));
            }
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException e) {
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();

    }
}
