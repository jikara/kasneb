/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "invoice")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 20)
@DiscriminatorValue(value = "DEFAULT")
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i"),
    @NamedQuery(name = "Invoice.findById", query = "SELECT i FROM Invoice i WHERE i.id = :id"),
    @NamedQuery(name = "Invoice.findByReference", query = "SELECT i FROM Invoice i WHERE i.status = :status")})
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "gbpAmount")
    private BigDecimal gbpTotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kesAmount")
    private BigDecimal kesTotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usdAmount")
    private BigDecimal usdTotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reference", nullable = false)
    private String reference;
    @Basic(optional = false)
    @Column(name = "dateGenerated", nullable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateGenerated;
    @OneToOne
    @JoinColumn(name = "invoiceStatusId", referencedColumnName = "status", nullable = false)
    private InvoiceStatus status = new InvoiceStatus("PENDING");
    @OneToMany(mappedBy = "invoice", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Collection<InvoiceDetail> invoiceDetails;
    @ManyToOne
    @JoinColumn(name = "feeCode", referencedColumnName = "code", nullable = false)
    @JsonManagedReference
    private FeeCode feeCode;
    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private Payment payment;
    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL)
    @JsonBackReference
    private StudentCourseSubscription studentCourseSubscription;
    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL)
    @JsonBackReference
    private StudentCourseSitting studentCourseSitting;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "studentCourseId")
    @JsonBackReference
    private StudentCourse studentCourse;

    public Invoice() {
    }

    public Invoice(String reference, Date dateGenerated) {
        this.reference = reference;
        this.dateGenerated = dateGenerated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDateGenerated() {
        return dateGenerated;
    }

    public void setDateGenerated(Date dateGenerated) {
        this.dateGenerated = dateGenerated;
    }

    public BigDecimal getKesTotal() {
        return kesTotal;
    }

    public void setKesTotal(BigDecimal kesTotal) {
        this.kesTotal = kesTotal;
    }

    public BigDecimal getGbpTotal() {
        return gbpTotal;
    }

    public void setGbpTotal(BigDecimal gbpTotal) {
        this.gbpTotal = gbpTotal;
    }

    public BigDecimal getUsdTotal() {
        return usdTotal;
    }

    public void setUsdTotal(BigDecimal usdTotal) {
        this.usdTotal = usdTotal;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public Collection<InvoiceDetail> getInvoiceDetails() {
        return invoiceDetails;
    }

    public void setInvoiceDetails(Collection<InvoiceDetail> invoiceDetails) {
        this.invoiceDetails = invoiceDetails;
    }

    public StudentCourseSitting getStudentCourseSitting() {
        return studentCourseSitting;
    }

    public void setStudentCourseSitting(StudentCourseSitting studentCourseSitting) {
        studentCourseSitting.setInvoice(this);
        this.studentCourseSitting = studentCourseSitting;
    }

    public FeeCode getFeeCode() {
        return feeCode;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public StudentCourseSubscription getStudentCourseSubscription() {
        return studentCourseSubscription;
    }

    public void setStudentCourseSubscription(StudentCourseSubscription studentCourseSubscription) {
        this.studentCourseSubscription = studentCourseSubscription;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    //Helper
    public void addInvoiceDetail(InvoiceDetail invoiceDetail) {
        if (invoiceDetail != null) {
            if (invoiceDetails == null) {
                invoiceDetails = new ArrayList<>();
            }
            invoiceDetails.add(invoiceDetail);
            invoiceDetail.setInvoice(this);
        }
    }

    public void setFeeCode(FeeCode feeCode) {
        this.feeCode = feeCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invoice)) {
            return false;
        }
        final Invoice other = (Invoice) object;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.Invoice[ id=" + id + " ]";
    }

}
