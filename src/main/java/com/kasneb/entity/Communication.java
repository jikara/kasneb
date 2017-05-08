/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "communication")
public class Communication implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "subject")
    private String subject;    
    @ManyToOne(optional=true,fetch=FetchType.LAZY)
    @JoinColumn(name = "studentId", updatable = false, referencedColumnName = "id")
    private Student student;    
    @ManyToOne(optional=true,fetch=FetchType.LAZY)
    @JoinColumn(name = "userId", updatable = false, referencedColumnName = "id")
    private User user;    
    @ManyToOne(optional=true,fetch=FetchType.LAZY)
    @JoinColumn(name = "studentCourseId", updatable = false, referencedColumnName = "id")
    private StudentCourse studentCourse;
    @ManyToOne(optional=true,fetch=FetchType.LAZY)
    @JoinColumn(name = "studentCourseSittingId", updatable = false, referencedColumnName = "id")
    private StudentCourseSitting sitting;
    @ManyToOne(optional=true,fetch=FetchType.LAZY)
    @JoinColumn(name = "exemptionId", updatable = false, referencedColumnName = "id")
    private Exemption exemption;
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(name = "communicationType", updatable = false, nullable = false)
    private CommunicationType type;
    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(name = "alertType", updatable = false, nullable = false)
    private AlertType alertType;
    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    private Boolean status;
    @ManyToOne(optional=true,fetch=FetchType.LAZY)
    @JoinColumn(name = "invoiceId", updatable = false, referencedColumnName = "id")
    private Invoice invoice;
    @Transient
    private String pin;

    public Communication() {
    }

    public Communication(Student student, StudentCourse studentCourse, StudentCourseSitting sitting, Exemption exemption, CommunicationType type, AlertType alertType, Boolean status) {
        this.student = student;
        this.studentCourse = studentCourse;
        this.sitting = sitting;
        this.exemption = exemption;
        this.type = type;
        this.alertType = alertType;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public StudentCourseSitting getSitting() {
        return sitting;
    }

    public void setSitting(StudentCourseSitting sitting) {
        this.sitting = sitting;
    }

    public Exemption getExemption() {
        return exemption;
    }

    public void setExemption(Exemption exemption) {
        this.exemption = exemption;
    }

    public CommunicationType getType() {
        return type;
    }

    public void setType(CommunicationType type) {
        this.type = type;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
