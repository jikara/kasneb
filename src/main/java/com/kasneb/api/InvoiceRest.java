/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.entity.Invoice;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("invoice")
@Stateless
public class InvoiceRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Hibernate5Module hbm = new Hibernate5Module();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.InvoiceFacade invoiceFacade;

    /**
     * Creates a new instance of InvoiceRest
     */
    public InvoiceRest() {
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.InvoiceRest
     *
     * @return an instance of javax.ws.rs.core.Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        List<Invoice> invoices = invoiceFacade.findAll();
        json = mapper.writeValueAsString(invoices);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@PathParam("id") Integer id) throws JsonProcessingException {
        Invoice invoice = invoiceFacade.find(id);
        invoice.getInvoiceDetails().size();
        json = mapper.writeValueAsString(invoice);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }
}
