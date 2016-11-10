/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kasneb.entity.pk.StudentCourseSubscriptionPK;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentCourseSubscription")
public class StudentCourseSubscription implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private StudentCourseSubscriptionPK studentCourseSubscriptionPK;
    @ManyToOne
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonBackReference
    private StudentCourse studentCourse;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created = new Date();
    @Basic(optional = false)
    @Column(name = "expiry", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expiry;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoiceId", referencedColumnName = "id")
    @JsonIgnore
    private Invoice invoice;
    @Basic(optional = false)
    @Column(name = "isCurrent", nullable = false)
    private Boolean current = false;

    public StudentCourseSubscription() {
    }

    public StudentCourseSubscription(StudentCourseSubscriptionPK studentCourseSubscriptionPK, Date expiry, Invoice invoice) {
        this.studentCourseSubscriptionPK = studentCourseSubscriptionPK;
        this.expiry = expiry;
        this.invoice = invoice;
    }

    public StudentCourseSubscription(StudentCourse studentCourse, Date expiry) {
        this.studentCourse = studentCourse;
        this.expiry = expiry;
    }

    public StudentCourseSubscriptionPK getStudentCourseSubscriptionPK() {
        return studentCourseSubscriptionPK;
    }

    public void setStudentCourseSubscriptionPK(StudentCourseSubscriptionPK studentCourseSubscriptionPK) {
        this.studentCourseSubscriptionPK = studentCourseSubscriptionPK;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Boolean isCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

}
