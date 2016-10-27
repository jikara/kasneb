/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author jikara
 */
public class ReceiptDetail implements Serializable {

    private ReceiptDetailPK receiptDetailPK;
    @JsonBackReference
    private Receipt receipt;
    private Course course;
    private ReceiptCategory category;
    private String description;
    private BigDecimal amount;
    @JsonIgnore
    private CpaRegistration registration;
    private String postingCode;
    private String fullRegNo;
    private Currency currency;

    public ReceiptDetail() {
    }

    public ReceiptDetail(ReceiptDetailPK receiptDetailPK, Receipt receipt, Course course, ReceiptCategory category, String description, BigDecimal amount, CpaRegistration registration, String postingCode,Currency currency) {
        this.receiptDetailPK = receiptDetailPK;
        this.receipt = receipt;
        this.course = course;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.registration = registration;
        this.postingCode = postingCode;
        this.currency = currency;
    }

    public ReceiptDetailPK getReceiptDetailPK() {
        return receiptDetailPK;
    }

    public void setReceiptDetailPK(ReceiptDetailPK receiptDetailPK) {
        this.receiptDetailPK = receiptDetailPK;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ReceiptCategory getCategory() {
        return category;
    }

    public void setCategory(ReceiptCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public CpaRegistration getRegistration() {
        return registration;
    }

    public void setRegistration(CpaRegistration registration) {
        this.registration = registration;
    }

    public String getPostingCode() {
        return postingCode;
    }

    public void setPostingCode(String postingCode) {
        this.postingCode = postingCode;
    }

    public String getFullRegNo() {
        return fullRegNo;
    }

    public void setFullRegNo(String fullRegNo) {
        this.fullRegNo = fullRegNo;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
