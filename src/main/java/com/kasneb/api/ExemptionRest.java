/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.kasneb.entity.Exemption;
import com.kasneb.entity.ExemptionPaper;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.VerificationStatus;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.lang.reflect.InvocationTargetException;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("exemption")
public class ExemptionRest {

    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    @EJB
    com.kasneb.session.ExemptionFacade exemptionFacade;
    @EJB
    com.kasneb.session.InvoiceFacade invoiceFacade;

    /**
     * Retrieves representation of an instance of com.kasneb.api.ExemptionRest
     *
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        anyResponse = exemptionFacade.findAll();
        return Response
                .status(Response.Status.OK)
                .entity(anyResponse)
                .build();
    }

    @GET
    @Path("pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPending() {
        anyResponse = exemptionFacade.findPending();
        return Response
                .status(Response.Status.OK)
                .entity(anyResponse)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id) {
        anyResponse = exemptionFacade.findPending(id);
        return Response
                .status(Response.Status.OK)
                .entity(anyResponse)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Exemption entity) {
        try {
            if (entity.getQualifications() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Qualification must be defined");
            }
            if (entity.getQualifications().size() < 1) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "At least one qualification must be specified");
            }
            //Combined.
            if (entity.getQualifications().get(0).getId().equals("1000")) {
                entity = exemptionFacade.createExemption(entity);
                //Do nothing
            } else {
                for (ExemptionPaper exemptionPaper : entity.getPapers()) {
                    exemptionPaper.setVerified(Boolean.TRUE);
                    exemptionPaper.setVerificationStatus(VerificationStatus.APPROVED);
                }
                entity = exemptionFacade.createExemption(entity);
                //Generate invoice
                Invoice invoice = invoiceFacade.generateExemptionInvoice(entity);
                invoice = invoiceFacade.find(invoice.getId());
                entity.setInvoice(invoice);
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
            // Logger.getLogger(ExemptionRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Exemption entity) throws IllegalAccessException, InvocationTargetException, CustomHttpException {
        boolean all = exemptionFacade.verifyAll(entity);
        Exemption managed = exemptionFacade.find(entity.getId());
        if (all) {
            invoiceFacade.generateExemptionInvoice(managed);
        }
        httpStatus = Response.Status.OK;
        anyResponse = managed;
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }
}
