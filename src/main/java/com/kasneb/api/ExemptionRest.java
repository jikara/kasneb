/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.entity.Exemption;
import com.kasneb.entity.ExemptionPaper;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.Paper;
import com.kasneb.entity.VerificationStatus;
import com.kasneb.entity.pk.ExemptionPaperPK;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
@Stateless
public class ExemptionRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Hibernate5Module hbm = new Hibernate5Module();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.ExemptionFacade exemptionFacade;
    @EJB
    com.kasneb.session.InvoiceFacade invoiceFacade;
    @EJB
    com.kasneb.session.PaperFacade paperFacade;

    public ExemptionRest() {
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.ExemptionRest
     *
     * @return an instance of javax.ws.rs.core.Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        anyResponse = exemptionFacade.findAll();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPending() throws JsonProcessingException {
        anyResponse = exemptionFacade.findPending();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id) throws JsonProcessingException {
        anyResponse = exemptionFacade.findPending(id);
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Exemption entity) throws JsonProcessingException {
        List<ExemptionPaper> papers = entity.getPapers();
        List<ExemptionPaper> exemptionPapers = new ArrayList<>();
        entity.setPapers(new ArrayList<>());
        try {
            if (entity.getQualifications() == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Qualification must be defined");
            }
            if (entity.getQualifications().size() < 1) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "At least one qualification must be specified");
            }
            exemptionFacade.create(entity);
            //Combined.
            if (entity.getQualifications().get(0).getId().equals("1000")) {
                entity = exemptionFacade.createExemption(entity);
                //Do nothing
            } else {
                for (ExemptionPaper exemptionPaper : papers) {
                    Paper paper = paperFacade.findPaper(exemptionPaper.getPaper().getCode());
                    ExemptionPaperPK pk = new ExemptionPaperPK(exemptionPaper.getPaper().getCode(), entity.getId());
                    exemptionPaper.setExemptionPaperPK(pk);
                    exemptionPaper.setVerified(Boolean.TRUE);
                    exemptionPaper.setPaper(paper);
                    exemptionPaper.setVerificationStatus(VerificationStatus.APPROVED);
                    exemptionPapers.add(exemptionPaper);
                    exemptionFacade.createExemptionPaper(exemptionPaper);
                }
                entity.setPapers(exemptionPapers);
                entity = exemptionFacade.createExemption(entity);
                //Generate invoice
                Invoice invoice = invoiceFacade.generateExemptionInvoice(entity);
                invoice = invoiceFacade.find(invoice.getId());
                entity.setInvoice(invoice);
            }
            anyResponse=exemptionFacade.find(entity.getId());
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
            // Logger.getLogger(ExemptionRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Exemption entity) throws IllegalAccessException, InvocationTargetException, CustomHttpException, JsonProcessingException {
        boolean all = exemptionFacade.verifyAll(entity);
        Exemption managed = exemptionFacade.find(entity.getId());
        if (all) {
            invoiceFacade.generateExemptionInvoice(managed);
        }
        httpStatus = Response.Status.OK;
        anyResponse = managed;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
}
