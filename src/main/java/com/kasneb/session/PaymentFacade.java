/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.kasneb.entity.Currency;
import com.kasneb.entity.ExemptionInvoice;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceStatus;
import com.kasneb.entity.Notification;
import com.kasneb.entity.NotificationStatus;
import com.kasneb.entity.NotificationType;
import com.kasneb.entity.Payment;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourseSittingStatus;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.CompletePaymentResponse;
import com.kasneb.model.LoginResponse;
import com.kasneb.model.StudentPayment;
import com.kasneb.model.PreparePaymentResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.EnumUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPut;

/**
 *
 * @author jikara
 */
@Stateless
public class PaymentFacade extends AbstractFacade<Payment> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    @EJB
    com.kasneb.session.NotificationFacade notificationFacade;
    private static final String CORPORATE_URL = "http://197.254.58.150/JamboPayServices/";
    private static final String APP_KEY = "F2A2A256-0C82-E511-9406-7427EA2F7F59";
    private static final String MERCHANT_CODE = "123456";

    private static final String REGISTRATION_FEE = "REGISTRATION_FEE";

    private static final String REGISTRATION_RENEWAL_FEE = "REGISTRATION_RENEWAL_FEE";

    private static final String EXAM_ENTRY_FEE = "EXAM_ENTRY_FEE";

    private static final String EXEMPTION_FEE = "EXEMPTION_FEE";

    private static final String PUBLICATION_FEE = "PUBLICATION_FEE";

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaymentFacade() {
        super(Payment.class);
    }

    public Payment createPayment(Payment entity) throws CustomHttpException {
        //Validat data
        if (entity.getAmount() == null) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Amount is required");
        }
        try {
            EnumUtils.isValidEnum(Currency.class, entity.getCurrency());
        } catch (java.lang.NullPointerException npe) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Invalid currency");
        }
        if (entity.getPhoneNumber() == null) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Wallet details are required exist");
        }
        //validate invoice exists
        Invoice invoice = em.find(Invoice.class, entity.getInvoice().getId());
        em.detach(invoice);
        if (invoice == null) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Bill does not exist");
        }
        if (invoice.getStatus().getStatus().equals("CANCELLED")) {
            throw new CustomHttpException(Status.CONFLICT, "Bill has been cancelled");
        }
        if (invoice.getStatus().getStatus().equals("PAID")) {
            throw new CustomHttpException(Status.CONFLICT, "Bill already paid");
        }
        switch (entity.getCurrency()) {
            case "KES":
                if (invoice.getKesTotal().compareTo(entity.getAmount()) != 0) {
                    throw new CustomHttpException(Status.NOT_ACCEPTABLE, "Payment amount must be equal to bill charged");
                }
                break;
            case "USD":
                if (invoice.getUsdTotal().compareTo(entity.getAmount()) != 0) {
                    throw new CustomHttpException(Status.NOT_ACCEPTABLE, "Payment amount must be equal to bill charged");
                }
                break;
            case "GBP":
                if (invoice.getGbpTotal().compareTo(entity.getAmount()) != 0) {
                    throw new CustomHttpException(Status.NOT_ACCEPTABLE, "Payment amount must be equal to bill charged");
                }
                break;
        }
        //complete jambopay payment
        completeJambopayPayment(entity, invoice);
        entity.setChannel("JAMBOPAY E_WALLET");
        em.persist(entity);
        Notification notification = null;
        //update related entities
        switch (invoice.getFeeCode().getCode()) {
            case EXEMPTION_FEE:
                ExemptionInvoice exemptionInvoice = (ExemptionInvoice) invoice;
                //Update exemptions
                exemptionInvoice.getExemptionInvoiceDetails().stream().forEach((detail) -> {
                    detail.getStudentCourseExemptionPaper().setPaid(Boolean.TRUE);
                    detail.getStudentCourseExemptionPaper().setDatePaid(new Date());
                });
                exemptionInvoice.setStatus(new InvoiceStatus("PAID"));
                em.merge(exemptionInvoice);
                break;
            case REGISTRATION_FEE:
                //update invoice as paid
                invoice.setStatus(new InvoiceStatus("PAID"));
                em.merge(invoice);
                notification = new Notification(NotificationStatus.UNREAD, NotificationType.PAYMENT, "Registration fee payment has been successfully processed", invoice.getStudentCourse().getStudent());
                break;
            case REGISTRATION_RENEWAL_FEE:
                //update invoice as paid
                invoice.setStatus(new InvoiceStatus("PAID"));
                em.merge(invoice);
                notification = new Notification(NotificationStatus.UNREAD, NotificationType.PAYMENT, "Registration renewal fee payment has been successfully processed", invoice.getStudentCourse().getStudent());
                break;
            case EXAM_ENTRY_FEE:
                invoice.getStudentCourseSitting().setStatus(StudentCourseSittingStatus.CONFIRMED);
                //update invoice as paid
                invoice.setStatus(new InvoiceStatus("PAID"));
                em.merge(invoice);
                notification = new Notification(NotificationStatus.UNREAD, NotificationType.PAYMENT, "Exam entry fee payment has been successfully processed", invoice.getStudentCourse().getStudent());
                break;
            case PUBLICATION_FEE:
                //update invoice as paid
                invoice.setStatus(new InvoiceStatus("PAID"));
                em.merge(invoice);
                notification = new Notification(NotificationStatus.UNREAD, NotificationType.PAYMENT, "Publication fee payment has been successfully processed", invoice.getStudentCourse().getStudent());
                break;
            default:
                break;
        }
        notificationFacade.create(notification);
        return entity;
    }

    private void completeJambopayPayment(Payment entity, Invoice invoice) throws CustomHttpException {
        try {  //Login
            LoginResponse loginResponse = getToken(entity.getPhoneNumber(), entity.getPin());
            //Prepare transaction
            PreparePaymentResponse preparePaymentResponse = preparePayment(loginResponse, entity, invoice);
            //Prepare transaction
            completePayment(loginResponse, preparePaymentResponse, entity.getPhoneNumber(), entity.getPin());
        } catch (IOException ex) {
            Logger.getLogger(PaymentFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        //  throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Jambopay payment method not set");
    }

    private static LoginResponse getToken(String username, String password) throws CustomHttpException, UnsupportedEncodingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        LoginResponse corporateLoginResponse;
        String line;
        String url = CORPORATE_URL + "Token";
        HttpClient client = new DefaultHttpClient();
        HttpPost put = new HttpPost(url);
        // add header
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "wallet"));
        urlParameters.add(new BasicNameValuePair("username", username));
        urlParameters.add(new BasicNameValuePair("password", password));
        put.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(put);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result.toString());
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "An error occured.Your payment could not be completed.");
        }
        corporateLoginResponse = mapper.readValue(result.toString(), LoginResponse.class);
        return corporateLoginResponse;
    }

    private static PreparePaymentResponse preparePayment(LoginResponse loginResponse, Payment entity, Invoice invoice) throws CustomHttpException, UnsupportedEncodingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        PreparePaymentResponse preparePaymentResponse;
        String line;
        String url = CORPORATE_URL + "api/Payments/Post";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        // add header
        post.setHeader("Authorization", "bearer " + loginResponse.getAccess_token());
        post.setHeader("app_key", APP_KEY);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("PhoneNumber", entity.getPhoneNumber()));
        urlParameters.add(new BasicNameValuePair("PaidBy", invoice.getStudentCourse().getStudent().getFirstName()));
        urlParameters.add(new BasicNameValuePair("PaymentTypeID", "1"));
        urlParameters.add(new BasicNameValuePair("Stream", "merchantpayment"));
        urlParameters.add(new BasicNameValuePair("Amount", String.valueOf(entity.getAmount())));
        urlParameters.add(new BasicNameValuePair("MerchantCode", MERCHANT_CODE));
        urlParameters.add(new BasicNameValuePair("ProductName", invoice.getFeeCode().getCode()));
        urlParameters.add(new BasicNameValuePair("InvoiceNumber", invoice.getReference()));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "An error occured.Your request could not be processed.");
        }
        preparePaymentResponse = mapper.readValue(result.toString(), PreparePaymentResponse.class);
        return preparePaymentResponse;
    }

    private static CompletePaymentResponse completePayment(LoginResponse loginResponse, PreparePaymentResponse preparePaymentResponse, String phoneNumber, String pin) throws UnsupportedEncodingException, IOException, CustomHttpException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        CompletePaymentResponse completePaymentResponse;
        String line;
        String url = CORPORATE_URL + "api/Payments/Put";
        HttpClient client = new DefaultHttpClient();
        HttpPut put = new HttpPut(url);
        // add header
        put.setHeader("Authorization", "bearer " + loginResponse.getAccess_token());
        put.setHeader("app_key", APP_KEY);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("Stream", "merchantpayment"));
        urlParameters.add(new BasicNameValuePair("PhoneNumber", phoneNumber));
        urlParameters.add(new BasicNameValuePair("TransactionID", preparePaymentResponse.getTransactionID()));
        urlParameters.add(new BasicNameValuePair("Pin", pin));
        put.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(put);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "An error occured.Your request could not be processed.");
        }
        completePaymentResponse = mapper.readValue(result.toString(), CompletePaymentResponse.class);
        return completePaymentResponse;
    }

    public Collection<Payment> findByStudent(Student student) {
        TypedQuery<Payment> query
                = em.createQuery("SELECT p FROM Payment p JOIN p.invoice i WHERE i.studentCourse.student =:student ORDER by p.paymentTimestamp DESC", Payment.class);
        query.setParameter("student", student);
        return query.getResultList();
    }

    public Collection<Payment> findByCode(String feeCode) {
        TypedQuery<Payment> query
                = em.createQuery("SELECT p FROM Payment p JOIN p.invoice i WHERE i.feeCode.code =:feeCode ORDER by p.paymentTimestamp DESC", Payment.class);
        query.setParameter("feeCode", feeCode);
        return query.getResultList();
    }

    public Collection<StudentPayment> getPaymentSummary() {
        Collection<StudentPayment> studentPayments = new ArrayList<>();
        TypedQuery<Student> query = em.createQuery("SELECT DISTINCT s FROM Student s JOIN s.studentCourses sc JOIN sc.invoices i JOIN i.payment p WHERE i.status.status = 'PAID'", Student.class);
        query.getResultList().stream().forEach((student) -> {
            studentPayments.add(new StudentPayment(student, findByStudent(student)));
        });
        return studentPayments;
    }

    public Collection<StudentPayment> getPaymentSummary(FeeCode code) {
        Collection<StudentPayment> studentPayments = new ArrayList<>();
        TypedQuery<Student> query = em.createQuery("SELECT DISTINCT s FROM Student s JOIN s.studentCourses sc JOIN sc.invoices i JOIN i.payment p WHERE i.status.status = 'PAID' AND i.feeCode =:feeCode", Student.class);
        query.setParameter("feeCode", code);
        query.getResultList().stream().forEach((student) -> {
            studentPayments.add(new StudentPayment(student, findByStudent(student)));
        });
        return studentPayments;
    }

    public Collection<StudentPayment> getPaymentSummary(Date startDate, Date endDate) {
        Collection<StudentPayment> studentPayments = new ArrayList<>();
        TypedQuery< Student> query = em.createQuery("SELECT DISTINCT s FROM Student s JOIN s.studentCourses sc JOIN sc.invoices i JOIN i.payment p WHERE i.status.status = 'PAID' AND p.paymentTimestamp BETWEEN :startDate AND :endDate",  Student.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.getResultList().stream().forEach((student) -> {
            studentPayments.add(new StudentPayment(student, findByStudent(student)));
        });
        return studentPayments;
    }

    public Collection<StudentPayment> getPaymentSummary(FeeCode code, Date startDate, Date endDate) {
        Collection<StudentPayment> studentPayments = new ArrayList<>();
        TypedQuery<Student> query = em.createQuery("SELECT DISTINCT s FROM Student s JOIN s.studentCourses sc JOIN sc.invoices i JOIN i.payment p WHERE i.status.status = 'PAID' AND i.feeCode =:feeCode AND p.paymentTimestamp BETWEEN :startDate AND :endDate",  Student.class);
        query.setParameter("feeCode", code);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.getResultList().stream().forEach((student) -> {
            studentPayments.add(new StudentPayment(student, findByStudent(student)));
        });
        return studentPayments;
    }

}
