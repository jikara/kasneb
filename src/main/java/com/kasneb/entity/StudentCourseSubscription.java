/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "rYear", nullable = false)
    private Integer year;
    @ManyToOne
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id", insertable = false, updatable = false)
    private StudentCourse studentCourse;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "created", nullable = false)
    private Date created = new Date();
    @Basic(optional = false)
    @Column(name = "expiry", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date expiry;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "invoiceId", referencedColumnName = "id")
    @JsonIgnore
    private Invoice invoice;
    @Basic(optional = false)
    @Column(name = "isCurrent", nullable = false)
    private Boolean current = false;

    public StudentCourseSubscription() {
    }

    public StudentCourseSubscription(StudentCourse studentCourse, Integer year, Date expiry, Invoice invoice) {
        this.studentCourse = studentCourse;
        this.year = year;
        this.expiry = expiry;
        this.invoice = invoice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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
