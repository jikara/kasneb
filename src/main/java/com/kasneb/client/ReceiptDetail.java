/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.math.BigDecimal;

/**
 *
 * @author jikara
 */
public class ReceiptDetail {

    @JsonBackReference
    private Receipt receipt;
    private Course course;
    private String lastUser;
    private String created;
    private String studentName;
    private ReceiptCategory category;
    private String description;
    private BigDecimal amount;
    @JsonBackReference
    private Registration registration;
    private String postingCode;
    private String fullRegNo;
    private Currency currency;

    public ReceiptDetail(Course course, String lastUser, String created, String studentName, ReceiptCategory category, String description, BigDecimal amount, Registration registration, String postingCode, String fullRegNo, Currency currency) {
        this.course = course;
        this.lastUser = lastUser;
        this.created = created;
        this.studentName = studentName;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.registration = registration;
        this.postingCode = postingCode;
        this.fullRegNo = fullRegNo;
        this.currency = currency;
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

    public String getLastUser() {
        return lastUser;
    }

    public void setLastUser(String lastUser) {
        this.lastUser = lastUser;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
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
