
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kasneb.entity.AlertType;
import com.kasneb.entity.Communication;
import com.kasneb.entity.CommunicationType;
import com.kasneb.entity.ExamCentre;
import com.kasneb.entity.Exemption;
import com.kasneb.entity.ExemptionStatus;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceDetail;
import com.kasneb.entity.InvoiceStatus;
import com.kasneb.entity.Payment;
import com.kasneb.entity.RenewalInvoiceDetail;
import com.kasneb.entity.StudentCourseSittingStatus;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import com.kasneb.util.Constants;
import com.kasneb.util.PredicateUtil;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("payment")
public class PaymentRest {

    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    @EJB
    com.kasneb.session.PaymentFacade paymentFacade;
    @EJB
    com.kasneb.session.NotificationFacade notificationFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;
    @EJB
    com.kasneb.session.StudentCourseSubscriptionFacade studentCourseSubscriptionFacade;
    @EJB
    com.kasneb.session.CommunicationFacade communicationFacade;
    @EJB
    com.kasneb.session.ExemptionFacade exemptionFacade;
    @EJB
    com.kasneb.session.InvoiceFacade invoiceFacade;
    @EJB
    private com.kasneb.session.ExamCentreFacade examCentreFacade;
    @EJB
    private com.kasneb.session.StudentCourseSittingFacade studentCourseSittingFacade;

    /**
     * Creates a new instance of PaymentRest
     */
    public PaymentRest() {
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Payment entity) {
        try {
            entity = paymentFacade.createWalletPayment(entity);
            Invoice invoice = invoiceFacade.find(entity.getInvoice().getId());
            //update related entities
            switch (invoice.getFeeCode().getCode()) {
                case Constants.EXEMPTION_FEE:
                    //Update exemptions
                    invoice.setStatus(new InvoiceStatus("PAID"));
                    invoiceFacade.edit(invoice);
                    //Update exempted papers as PAID
                    exemptionFacade.updatePaidPapers(invoice);
                    //Push exemptions to Kasneb
                    Exemption managed = invoice.getExemption();
                    managed.setStatus(ExemptionStatus.COMPLETED);
                    exemptionFacade.edit(managed);
                    exemptionFacade.createCoreExemption(managed);
                    Communication communication = new Communication(invoice.getStudentCourse().getStudent(), invoice.getStudentCourse(), null, invoice.getExemption(), CommunicationType.EXEMPTION_VERIFICATION, AlertType.SMS, false);
                    communication.setInvoice(invoice);
                    communicationFacade.create(communication);
                    communication.setAlertType(AlertType.EMAIL);
                    communicationFacade.create(communication);
                    break;
                case Constants.REGISTRATION_FEE:
                    //update invoice as paid
                    invoice.setStatus(new InvoiceStatus("PAID"));
                    invoiceFacade.edit(invoice);
                    break;
                case Constants.REGISTRATION_RENEWAL_FEE:
                    //update invoice as paid
                    invoice.setStatus(new InvoiceStatus("PAID"));
                    invoiceFacade.edit(invoice);
                    studentCourseSubscriptionFacade.createSubscriptions(invoice);
                    communication = new Communication(invoice.getStudentCourse().getStudent(), invoice.getStudentCourse(), null, null, CommunicationType.RENEWAL_PAYMENT, AlertType.EMAIL, false);
                    communication.setInvoice(invoice);
                    communicationFacade.create(communication);
                    communication.setAlertType(AlertType.EMAIL);
                    communicationFacade.create(communication);
                    break;
                case Constants.EXAM_ENTRY_FEE:
                    invoice.getStudentCourseSitting().setStatus(StudentCourseSittingStatus.PAID);
                    for (InvoiceDetail invDetail : invoice.getInvoiceDetails()) {
                        if (invDetail instanceof RenewalInvoiceDetail) {
                            studentCourseSubscriptionFacade.createSubscription((RenewalInvoiceDetail) invDetail);
                        }
                    }
                    //update invoice as paid
                    invoice.setStatus(new InvoiceStatus("PAID"));
                    invoiceFacade.edit(invoice);
                    break;
                case Constants.PUBLICATION_FEE:
                    //update invoice as paid
                    invoice.setStatus(new InvoiceStatus("PAID"));
                    invoiceFacade.edit(invoice);
                    break;
                default:
                    break;
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            // Logger.getLogger(PaymentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            // Logger.getLogger(PaymentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @GET
    @Path("bill")
    @Produces(MediaType.APPLICATION_JSON)
    public Response prepare(@Context HttpHeaders headers, @QueryParam("bill_number") String reference) throws IOException {
        Invoice invoice = null;
        try {
            invoice = invoiceFacade.findByReference(reference);
            List<ExamCentre> examCentres = examCentreFacade.findCentres(invoice.getStudentCourse());
            if (invoice.getFeeCode().getCode().equals(Constants.EXAM_ENTRY_FEE)) {
                invoice.setExamCentres(examCentres);
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
            // Logger.getLogger(PaymentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(invoice)
                .build();
    }

    @PUT
    @Path("advice")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response complete(@Context HttpHeaders headers, Payment entity) throws JsonProcessingException {
        try {
            String appKey;
            if (headers.getRequestHeader(Constants.DEVICE_HEADER_NAME) != null && !headers.getRequestHeader(Constants.DEVICE_HEADER_NAME).get(0).isEmpty()) {
                appKey = headers.getRequestHeader(Constants.DEVICE_HEADER_NAME).get(0);
                if (PredicateUtil.isSet(appKey) && !appKey.equals(Constants.BANK_APP_KEY)) {
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid app-key header");
                }
                if (PredicateUtil.isSet(appKey) && !appKey.equals(Constants.MOBILE_APP_KEY)) {
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid app-key header");
                }
                if (PredicateUtil.isSet(appKey) && !appKey.equals(Constants.WEB_APP_KEY)) {
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid app-key header");
                }
            } else {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No app-key header");
            }
            Invoice invoice = invoiceFacade.find(entity.getInvoice().getId());
            if (PredicateUtil.isSet(appKey) && appKey.equals(Constants.BANK_APP_KEY)) {
                if (invoice.getFeeCode().getCode().equals(Constants.EXAM_ENTRY_FEE) && entity.getCentre() == null) {
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Center must be specified for exam entry payment");
                }
            }
            entity = paymentFacade.createBankPayment(entity);
            entity.setCentre(entity.getCentre());
            //update related entities
            switch (invoice.getFeeCode().getCode()) {
                case Constants.EXEMPTION_FEE:
                    //Update exemptions
                    invoice.setStatus(new InvoiceStatus("PAID"));
                    invoiceFacade.edit(invoice);
                    //Update exempted papers as PAID
                    exemptionFacade.updatePaidPapers(invoice);
                    //Push exemptions to Kasneb
                    exemptionFacade.createCoreExemption(invoice.getExemption());
                    Communication communication = new Communication(invoice.getStudentCourse().getStudent(), invoice.getStudentCourse(), null, invoice.getExemption(), CommunicationType.EXEMPTION_VERIFICATION, AlertType.SMS, false);
                    communication.setInvoice(invoice);
                    communicationFacade.create(communication);
                    communication.setAlertType(AlertType.EMAIL);
                    communicationFacade.create(communication);
                    break;
                case Constants.REGISTRATION_FEE:
                    //update invoice as paid
                    invoice.setStatus(new InvoiceStatus("PAID"));
                    invoiceFacade.edit(invoice);
                    break;
                case Constants.REGISTRATION_RENEWAL_FEE:
                    //update invoice as paid
                    invoice.setStatus(new InvoiceStatus("PAID"));
                    invoiceFacade.edit(invoice);
                    studentCourseSubscriptionFacade.createSubscriptions(invoice);
                    communication = new Communication(invoice.getStudentCourse().getStudent(), invoice.getStudentCourse(), null, null, CommunicationType.RENEWAL_PAYMENT, AlertType.SMS, false);
                    communication.setInvoice(invoice);
                    communicationFacade.create(communication);
                    communication.setAlertType(AlertType.EMAIL);
                    communicationFacade.create(communication);
                    break;
                case Constants.EXAM_ENTRY_FEE:
                    if (PredicateUtil.isSet(appKey) && appKey.equals(Constants.BANK_APP_KEY)) {
                        invoice.getStudentCourseSitting().setSittingCentre(entity.getCentre());
                        invoice.getStudentCourseSitting().setStatus(StudentCourseSittingStatus.CONFIRMED);
                    }
                    for (InvoiceDetail invDetail : invoice.getInvoiceDetails()) {
                        if (invDetail instanceof RenewalInvoiceDetail) {
                            studentCourseSubscriptionFacade.createSubscription((RenewalInvoiceDetail) invDetail);
                        }
                    }
                    //update invoice as paid
                    invoice.setStatus(new InvoiceStatus("PAID"));
                    invoiceFacade.edit(invoice);
                    break;
                case Constants.PUBLICATION_FEE:
                    //update invoice as paid
                    invoice.setStatus(new InvoiceStatus("PAID"));
                    invoiceFacade.edit(invoice);
                    break;
                default:
                    break;
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            // Logger.getLogger(PaymentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException ex) {
            // Logger.getLogger(PaymentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }
}
