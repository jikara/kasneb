/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.math.BigDecimal;
import java.util.Collection;

/**
 *
 * @author jikara
 */
public class Receipt {

    private String receiptNo;
    private Course course;
    private String receivedFrom;
    private String regNo;
    @JsonBackReference
    private Registration registration;
    private String fullRegistrationNumber;
    private String lastUser;
    private String mdate;
    private String paymentType;
    private BigDecimal amount;
    private String referenceNumber;
    private Currency currency;
    private BigDecimal amount2;
    @JsonManagedReference
    private Collection<ReceiptDetail> receiptDetails;

    public Receipt(Course course, String receivedFrom, String regNo, String fullRegistrationNumber, String lastUser, String mdate, BigDecimal amount, Currency currency, BigDecimal amount2) {
        this.course = course;
        this.receivedFrom = receivedFrom;
        this.regNo = regNo;
        this.fullRegistrationNumber = fullRegistrationNumber;
        this.lastUser = lastUser;
        this.mdate = mdate;
        this.amount = amount;
        this.currency = currency;
        this.amount2 = amount2;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public String getFullRegistrationNumber() {
        return fullRegistrationNumber;
    }

    public void setFullRegistrationNumber(String fullRegistrationNumber) {
        this.fullRegistrationNumber = fullRegistrationNumber;
    }

    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount2() {
        return amount2;
    }

    public void setAmount2(BigDecimal amount2) {
        this.amount2 = amount2;
    }

    public Collection<ReceiptDetail> getReceiptDetails() {
        return receiptDetails;
    }

    public void setReceiptDetails(Collection<ReceiptDetail> receiptDetails) {
        for (ReceiptDetail receiptDetail : receiptDetails) {
            receiptDetail.setReceipt(this);
        }
        this.receiptDetails = receiptDetails;
    }
}
