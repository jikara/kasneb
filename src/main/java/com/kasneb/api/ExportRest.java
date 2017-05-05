/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.Exemption;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
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
    @Context
    HttpServletResponse httpServletResponse;
    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.ExportFacade exportFacade;
    @EJB
    com.kasneb.session.ExemptionFacade exemptionFacade;

    public ExportRest() {
    }

    @GET
    @Path("receipt/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void getReceipt(@PathParam("id") Integer transactionId) {
        ServletOutputStream outputStream;
        try {
            InputStream inputStream = null;
            List<com.kasneb.jasper.ReceiptDocument> receipts = new ArrayList();
            com.kasneb.jasper.ReceiptDocument receipt = exportFacade.generateReceipt(transactionId);
            if (receipt != null) {
                receipts.add(receipt);
                if (receipt.getType() == null) {
                    inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/receipt.jasper");
                } else {
                    switch (receipt.getType()) {
                        case Constants.EXAM_ENTRY_FEE:
                            inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/exam_receipt.jasper");
                            break;
                        case Constants.REGISTRATION_FEE:
                            inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/registration_receipt.jasper");
                            break;
                    }
                }
            }
            Map<String, Object> parameters = new HashMap<>();
            String realPath = servletContext.getRealPath("/");
            parameters.put("SUBREPORT_DIR", realPath);
            outputStream = httpServletResponse.getOutputStream();
            JasperRunManager.runReportToPdfStream(inputStream, outputStream, parameters, new JRBeanCollectionDataSource(receipts));
            httpServletResponse.setContentType("application/pdf");
            outputStream.flush();
            outputStream.close();
        } catch (JRException | IOException | CustomHttpException | ParseException ex) {
           // Logger.getLogger(ExportRest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("invoice/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void getInvoice(@PathParam("id") Integer invoiceId) {
        ServletOutputStream outputStream;
        try {
            com.kasneb.jasper.InvoiceDocument invoice = exportFacade.generateInvoice(invoiceId);
            List<com.kasneb.jasper.InvoiceDocument> invoices = new ArrayList();
            invoices.add(invoice);
            Map<String, Object> parameters = new HashMap<>();
            String realPath = servletContext.getRealPath("/");
            parameters.put("SUBREPORT_DIR", realPath);
            System.out.println(parameters);
            InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/invoice.jasper");
            outputStream = httpServletResponse.getOutputStream();
            JasperRunManager.runReportToPdfStream(inputStream, outputStream, parameters, new JRBeanCollectionDataSource(invoices));
            httpServletResponse.setContentType("application/pdf");
            outputStream.flush();
            outputStream.close();
        } catch (JRException | ParseException | IOException | CustomHttpException ex) {
           // Logger.getLogger(ExportRest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("timetable/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void getTimetable(@PathParam("id") Integer studentCourseSittingId) {
        ServletOutputStream outputStream;
        try {
            com.kasneb.jasper.TimetableDocument timetable = exportFacade.generateTimetable(studentCourseSittingId);
            List<com.kasneb.jasper.TimetableDocument> timetables = new ArrayList();
            timetables.add(timetable);
            Map<String, Object> parameters = new HashMap<>();
            String realPath = servletContext.getRealPath("/");
            parameters.put("SUBREPORT_DIR", realPath);
            InputStream inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/timetable.jasper");
            outputStream = httpServletResponse.getOutputStream();
            JasperRunManager.runReportToPdfStream(inputStream, outputStream, parameters, new JRBeanCollectionDataSource(timetables));
            httpServletResponse.setContentType("application/pdf");
            outputStream.flush();
            outputStream.close();
        } catch (JRException | IOException | CustomHttpException | ParseException ex) {
           // Logger.getLogger(ExportRest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GET
    @Path("exemptionletter/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public void getExemptionLetter(@PathParam("id") Integer exemptionId) {
        Exemption managed = exemptionFacade.find(exemptionId);
        ServletOutputStream outputStream;
        try {
            List<com.kasneb.jasper.ExemptionDocument> documents = new ArrayList();
            com.kasneb.jasper.ExemptionDocument exemptionLetter = exportFacade.generateExemptionLetter(managed);
            documents.add(exemptionLetter);
            Map<String, Object> parameters = new HashMap<>();
            String realPath = servletContext.getRealPath("/");
            parameters.put("SUBREPORT_DIR", realPath);
            InputStream inputStream = null;
            switch (managed.getStudentCourse().getCourse().getCourseTypeCode()) {
                case 100:
                    inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/provisional_exemption_letter_pro.jasper");
                    break;
                case 200:
                    inputStream = servletContext.getResourceAsStream("/WEB-INF/jasper/provisional_exemption_letter_dip.jasper");
                    break;
            }
            outputStream = httpServletResponse.getOutputStream();
            JasperRunManager.runReportToPdfStream(inputStream, outputStream, parameters, new JRBeanCollectionDataSource(documents));
            httpServletResponse.setContentType("application/pdf");
            outputStream.flush();
            outputStream.close();
        } catch (JRException | IOException | CustomHttpException | ParseException ex) {
           // Logger.getLogger(ExportRest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
