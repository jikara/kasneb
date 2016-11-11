/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
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
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("export")
public class ExportRest {

    @Context
    private UriInfo context;
    @Context
    ServletContext servletContext;
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
        try {
            com.kasneb.jasper.ReceiptDocument receipt = exportFacade.generateReceipt(receiptNumber);
            List<com.kasneb.jasper.ReceiptDocument> receipts = new ArrayList();
            receipts.add(receipt);
            Map<String, Object> parameters = new HashMap<>();
            // parameters.put("RECEIPT_DETAILS", receipt.getItems());
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(receipts);
            InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/Receipt.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\jikara\\Documents\\KASNEB\\receipt.pdf");
            httpStatus = Response.Status.OK;
        } catch (JRException ex) {
            Logger.getLogger(ExportRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(null)
                .type("application/pdf")
                .build();
    }

    @GET
    @Path("invoice/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getInvoice(@PathParam("id") String receiptNumber) {
        try {
            com.kasneb.jasper.ReceiptDocument receipt = exportFacade.generateReceipt(receiptNumber);
            List<com.kasneb.jasper.ReceiptDocument> receipts = new ArrayList();
            receipts.add(receipt);
            Map<String, Object> parameters = new HashMap<>();
            // parameters.put("RECEIPT_DETAILS", receipt.getItems());
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(receipts);
            InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/Receipt.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\jikara\\Documents\\KASNEB\\receipt.pdf");
            httpStatus = Response.Status.OK;
        } catch (JRException ex) {
            Logger.getLogger(ExportRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(null)
                .type("application/pdf")
                .build();
    }

    @GET
    @Path("timetable/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getTimetable(@PathParam("id") String id) {
        try {
            com.kasneb.jasper.TimetableDocument timetable = exportFacade.generateTimetable(1);
            List<com.kasneb.jasper.TimetableDocument> timetables = new ArrayList();
            timetables.add(timetable);
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(timetables);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("PAPERS", timetable.getPapers());
            InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/Timetable.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, beanCollectionDataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\jikara\\Documents\\KASNEB\\timetable1.pdf");
            httpStatus = Response.Status.OK;
        } catch (JRException ex) {
            Logger.getLogger(ExportRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(null)
                .type("application/pdf")
                .build();
    }

    @GET
    @Path("exemptionletter/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getExemptionLetter() {
        try {
            List<com.kasneb.jasper.ExemptionDocument> documentArray = new ArrayList();
            com.kasneb.jasper.ExemptionDocument exemptionLetter = exportFacade.generateExemptionLetter(1);
            documentArray.add(exemptionLetter);
            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(documentArray);
            InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/ExemptionLetter.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, new HashMap<>(), beanCollectionDataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, "C:\\Users\\jikara\\Documents\\KASNEB\\exemption_letter.pdf");
            httpStatus = Response.Status.OK;
        } catch (JRException ex) {
            Logger.getLogger(ExportRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(null)
                .type("application/pdf")
                .build();
    }
}
