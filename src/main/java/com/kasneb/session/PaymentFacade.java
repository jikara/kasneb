/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.client.Receipt;
import com.kasneb.client.ReceiptCategory;
import com.kasneb.client.ReceiptDetail;
import com.kasneb.client.Registration;
import com.kasneb.entity.Currency;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceDetail;
import com.kasneb.entity.Payment;
import com.kasneb.entity.PaymentDetail;
import com.kasneb.entity.Student;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.LoginResponse;
import com.kasneb.model.PreparePaymentResponse;
import com.kasneb.util.Constants;
import com.kasneb.util.CoreUtil;
import com.kasneb.util.DateUtil;
import com.kasneb.util.GeneratorUtil;
import com.kasneb.util.WalletUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response.Status;
import org.apache.commons.lang3.EnumUtils;

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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaymentFacade() {
        super(Payment.class);
    }

    public Payment createBankPayment(Payment entity) throws CustomHttpException, IOException, ParseException {
        //Validat data
        if (entity.getAmount() == null) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Amount is required");
        }
        try {
            EnumUtils.isValidEnum(Currency.class, entity.getCurrency());
        } catch (java.lang.NullPointerException npe) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Invalid currency");
        }
        //validate invoice exists
        Invoice invoice = em.find(Invoice.class, entity.getInvoice().getId());
        if (invoice == null) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Bill does not exist");
        }
        if (invoice.getStatus().getStatus().equals("CANCELLED")) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Bill has been cancelled");
        }
        if (invoice.getStatus().getStatus().equals("PAID")) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Bill already paid");
        }
        switch (entity.getCurrency()) {
            case "KES":
                if (invoice.getKesTotal().compareTo(entity.getTotalAmount()) != 0) {
                    throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Payment amount must be equal to bill charged");
                }
                break;
            case "USD":
                if (invoice.getUsdTotal().compareTo(entity.getTotalAmount()) != 0) {
                    throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Payment amount must be equal to bill charged");
                }
                break;
            case "GBP":
                if (invoice.getGbpTotal().compareTo(entity.getTotalAmount()) != 0) {
                    throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Payment amount must be equal to bill charged");
                }
                break;
        }
        Collection<PaymentDetail> paymentDetails = new ArrayList<>();
        for (InvoiceDetail invoiceDetail : invoice.getInvoiceDetails()) {
            paymentDetails.add(new PaymentDetail(invoiceDetail.getKesAmount(), invoice.getFeeCode(), invoiceDetail.getDescription()));
        }
        entity.setKasnebRef("");
        entity.setPaymentTimestamp(new Date());
        entity.setStudentCourse(invoice.getStudentCourse());
        entity.setPaymentDetails(paymentDetails);
        entity.setReceiptNo(GeneratorUtil.generateReceiptNumber());
        em.persist(entity);
        return entity;
    }

    public Payment createWalletPayment(Payment entity) throws CustomHttpException, IOException, ParseException {
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
        entity.setPhoneNumber("254" + entity.getPhoneNumber().substring(Math.max(entity.getPhoneNumber().length() - 9, 0)));
        //validate invoice exists
        Invoice invoice = em.find(Invoice.class, entity.getInvoice().getId());
        em.detach(invoice);
        if (invoice == null) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Bill does not exist");
        }
        if (invoice.getStatus().getStatus().equals("CANCELLED")) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Bill has been cancelled");
        }
        if (invoice.getStatus().getStatus().equals("PAID")) {
            throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Bill already paid");
        }
        switch (entity.getCurrency()) {
            case "KES":
                if (invoice.getKesTotal().compareTo(entity.getTotalAmount()) != 0) {
                    throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Payment amount must be equal to bill charged");
                }
                break;
            case "USD":
                if (invoice.getUsdTotal().compareTo(entity.getTotalAmount()) != 0) {
                    throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Payment amount must be equal to bill charged");
                }
                break;
            case "GBP":
                if (invoice.getGbpTotal().compareTo(entity.getTotalAmount()) != 0) {
                    throw new CustomHttpException(Status.INTERNAL_SERVER_ERROR, "Payment amount must be equal to bill charged");
                }
                break;
        }
        Collection<PaymentDetail> paymentDetails = new ArrayList<>();
        for (InvoiceDetail invoiceDetail : invoice.getInvoiceDetails()) {
            paymentDetails.add(new PaymentDetail(invoiceDetail.getKesAmount(), invoice.getFeeCode(), invoiceDetail.getDescription()));
        }
        entity.setChannel("KASNEB E_WALLET");
        entity.setKasnebRef("");
        entity.setPaymentTimestamp(new Date());
        entity.setStudentCourse(invoice.getStudentCourse());
        entity.setPaymentDetails(paymentDetails);
        entity.setReceiptNo(GeneratorUtil.generateReceiptNumber());
        //completeJambopayPayment(entity, invoice);
        em.persist(entity);
        return entity;
    }

    public void completeJambopayPayment(Payment entity, Invoice invoice) throws CustomHttpException {
        try {  //Login
            LoginResponse loginResponse = WalletUtil.getWalletToken(entity.getPhoneNumber(), entity.getPin());
            //Prepare transaction
            PreparePaymentResponse preparePaymentResponse = WalletUtil.preparePayment(loginResponse, entity, invoice);
            //Prepare transaction
            WalletUtil.completePayment(loginResponse, preparePaymentResponse, entity.getPhoneNumber(), entity.getPin());
        } catch (IOException ex) {
            // Logger.getLogger(PaymentFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Collection<Payment> findUnsynchronized() {
        TypedQuery<Payment> query
                = em.createNamedQuery("Payment.findUnsynchronized", Payment.class);
        query.setMaxResults(40);
        return query.getResultList();
    }

    public Collection<Payment> findAll(Student student) {
        TypedQuery<Payment> query
                = em.createNamedQuery("Payment.findByStudent", Payment.class);
        query.setParameter("student", student);
        query.setMaxResults(40);
        return query.getResultList();
    }

    public Collection<Payment> findAll(Student student, FeeCode feeCode) {
        TypedQuery<Payment> query
                = em.createNamedQuery("Payment.findByStudentAndCode", Payment.class);
        query.setParameter("feeCode", feeCode);
        query.setParameter("student", student);
        query.setMaxResults(40);
        return query.getResultList();
    }

    public Collection<Payment> findAll(Student student, Date startDate, Date endDate) {
        TypedQuery<Payment> query
                = em.createNamedQuery("Payment.findAll", Payment.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("student", student);
        query.setMaxResults(40);
        return query.getResultList();
    }

    public Collection<Payment> getPaymentSummary() {
        TypedQuery<Payment> query = em.createNamedQuery("Payment.getSummary", Payment.class);
        query.setParameter("status", "PAID");
        query.setMaxResults(40);
        return query.getResultList();
    }

    public Collection<Payment> getPaymentSummary(FeeCode code) {
        TypedQuery<Payment> query = em.createNamedQuery("Payment.findByFeeCode", Payment.class);
        query.setParameter("code", code);
        query.setParameter("status", "PAID");
        query.setMaxResults(40);
        return query.getResultList();
    }

    public Collection<Payment> getPaymentSummary(Date startDate, Date endDate) {
        TypedQuery<Payment> query = em.createNamedQuery("Payment.findByDateRange", Payment.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("status", "PAID");
        query.setMaxResults(40);
        return query.getResultList();
    }

    public Collection<Payment> getPaymentSummary(FeeCode code, Date startDate, Date endDate) {
        TypedQuery<Payment> query = em.createNamedQuery("Payment.findByCodeAndDate", Payment.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("code", code);
        query.setParameter("status", "PAID");
        query.setMaxResults(40);
        return query.getResultList();
    }

    public Receipt createReceipt(Payment payment) throws ParseException, IOException, CustomHttpException {
        String category = null;
        Invoice invoice = invoiceFacade.find(payment.getInvoice().getId());
        switch (invoice.getFeeCode().getCode()) {
            case Constants.REGISTRATION_FEE:
                category = "REG";
                break;
            case Constants.EXAM_ENTRY_FEE:
                category = "EXAM";
                break;
            case Constants.EXEMPTION_FEE:
                category = "EXEMP";
                break;
            case Constants.REGISTRATION_RENEWAL_FEE:
                category = "REN";
                break;
        }
        Collection<ReceiptDetail> receiptDetails = new ArrayList<>();
        String currency = "KSH";
        if (payment.getCurrency().equals("KES")) {
            currency = "KSH";
        }
        String studentName = payment.getStudentCourse().getStudent().getFullName();
        Receipt receipt = new Receipt(new com.kasneb.client.Course(payment.getStudentCourse().getCourse().getId()), studentName, String.valueOf(payment.getStudentCourse().getRegistrationNumber()), "", "MOBILE", DateUtil.getString(new Date()), payment.getAmount(), new com.kasneb.client.Currency(currency), new BigDecimal(0));
        for (PaymentDetail paymentDetail : payment.getPaymentDetails()) {
            //Receipt receipt, Course course, String lastUser, Date created, String studentName, ReceiptCategory category, String description, BigDecimal amount, Registration registration, String postingCode, String fullRegNo, Currency currency            
            ReceiptDetail rcpDetail = new ReceiptDetail(receipt.getCourse(), "MOBILE", DateUtil.getString(new Date()), studentName, new ReceiptCategory(category), paymentDetail.getDescription(), paymentDetail.getAmount(), new Registration(payment.getStudentCourse().getRegistrationNumber()), "", "", new com.kasneb.client.Currency(currency));
            if (!rcpDetail.getDescription().equals("Administrative fee")) {
                receiptDetails.add(rcpDetail);
            }
        }
        receipt.setReceiptDetails(receiptDetails);
        if (receipt.getAmount().compareTo(BigDecimal.ZERO) == 1) {
            receipt = CoreUtil.createCoreReceipt(receipt, payment.getStudentCourse().getCourse());
        }
        return receipt;
    }
}
