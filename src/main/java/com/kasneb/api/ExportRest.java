/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.util.ExportUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sf.jasperreports.engine.JRException;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("export")
public class ExportRest {

    @Context
    private UriInfo context;
    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.ExportFacade exportFacade;

    /**
     * Creates a new instance of ExportRest
     */
    public ExportRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.ExportRest
     *
     * @param receiptNumber
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Path("receipt/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getReceipt(@PathParam("id") String receiptNumber) {
        InputStream is = null;
        try {
            com.kasneb.jasper.ReceiptDocument receipt = exportFacade.generateReceipt(receiptNumber);
            is = new ExportUtil().generateReceipt(receipt);
            httpStatus = Response.Status.OK;
        } catch (IOException | JRException ex) {
            Logger.getLogger(ExportRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(is)
                .type("application/pdf")
                .build();
    }

    /**
     * PUT method for updating or creating an instance of ExportRest
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(Response content) {
    }
}
