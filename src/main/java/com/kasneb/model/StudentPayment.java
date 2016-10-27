/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.model;

import com.kasneb.entity.Payment;
import com.kasneb.entity.Student;
import java.util.Collection;

/**
 *
 * @author jikara
 */
public class StudentPayment {

    private Student student;
    private Integer studentId;
    private Collection<Payment> payments;

    public StudentPayment() {
    }

    public StudentPayment(Student student, Collection<Payment> payments) {
        this.student = student;
        this.payments = payments;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getStudentId() {
        if (getStudent() != null) {
            studentId = getStudent().getId();
        }
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
