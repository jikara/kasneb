/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.model;

import com.kasneb.entity.Payment;
import java.util.Collection;

/**
 *
 * @author jikara
 */
public class StudentPaymentSummary {

    private Integer studentId;
    private Collection<Payment> payments;

    public StudentPaymentSummary() {
    }

    public StudentPaymentSummary(Integer studentId, Collection<Payment> payments) {
        this.studentId = studentId;
        this.payments = payments;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Collection<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Collection<Payment> payments) {
        this.payments = payments;
    }

}
