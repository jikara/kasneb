/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "invoice")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 20)
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i")
    ,@NamedQuery(name = "Invoice.findById", query = "SELECT i FROM Invoice i WHERE i.id = :id")
    ,@NamedQuery(name = "Invoice.findByReference", query = "SELECT i FROM Invoice i WHERE i.status = :status")})
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    private Date dateGenerated;
    @ManyToOne
    @JoinColumn(name = "invoiceStatusId", referencedColumnName = "status", nullable = false)
    private InvoiceStatus status = new InvoiceStatus("PENDING");
    @OneToMany(mappedBy = "invoice", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InvoiceDetail> invoiceDetails;
    @ManyToOne
    @JoinColumn(name = "feeCode", referencedColumnName = "code", nullable = false)
    private FeeCode feeCode;
    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;
    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StudentCourseSubscription studentCourseSubscription;
    @OneToOne(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private StudentCourseSitting studentCourseSitting;
    @OneToOne(mappedBy = "invoice", fetch = FetchType.LAZY)
    private Exemption exemption;
    @ManyToOne
    @JoinColumn(name = "studentCourseId")
    private StudentCourse studentCourse;
    @OneToMany(mappedBy = "invoice", targetEntity = InvoiceDetail.class, orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<RenewalInvoiceDetail> renewalInvoiceDetails;
    @OneToMany(mappedBy = "invoice", targetEntity = InvoiceDetail.class, orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<ExemptionInvoiceDetail> exemptionInvoiceDetails;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    @Column(name = "dueDate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dueDate;
    @Transient
    private BigDecimal localAmount;
    @Transient
    private Currency localCurrency;
    @Transient
    private List<ExamCentre> examCentres;
    @Transient
    private BigDecimal netAmount;

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

    public List<InvoiceDetail> getInvoiceDetails() {
        if (invoiceDetails == null) {
            return null;
        }
        Collections.sort(invoiceDetails, new Comparator<InvoiceDetail>() {
            @Override
            public int compare(InvoiceDetail o1, InvoiceDetail o2) {
                if (o1 instanceof ExemptionInvoiceDetail && o2 instanceof ExemptionInvoiceDetail) {
                    ExemptionInvoiceDetail invDet1 = (ExemptionInvoiceDetail) o1;
                    ExemptionInvoiceDetail invDet2 = (ExemptionInvoiceDetail) o2;
                    return invDet1.getPaper().getPaper().getCode().compareTo(invDet2.getPaper().getPaper().getCode());
                }
                if (o1 instanceof RenewalInvoiceDetail && o2 instanceof RenewalInvoiceDetail) {
                    RenewalInvoiceDetail invDet1 = (RenewalInvoiceDetail) o1;
                    RenewalInvoiceDetail invDet2 = (RenewalInvoiceDetail) o2;
                    return invDet1.getYear().compareTo(invDet2.getYear());
                }
                if (o1 instanceof ExamInvoiceDetail && o2 instanceof ExamInvoiceDetail) {
                    ExamInvoiceDetail invDet1 = (ExamInvoiceDetail) o1;
                    ExamInvoiceDetail invDet2 = (ExamInvoiceDetail) o2;
                    if (invDet1.getPaper() != null && invDet2.getPaper() != null) {
                        return invDet1.getPaper().getCode().compareTo(invDet2.getPaper().getCode());
                    }
                    if (invDet1.getSection() != null && invDet2.getSection() != null) {
                        return invDet1.getSection().getSectionPK().getId().compareTo(invDet2.getSection().getSectionPK().getId());
                    }
                    if (invDet1.getLevel() != null && invDet2.getLevel() != null) {
                        return invDet1.getLevel().getLevelPK().getId().compareTo(invDet2.getLevel().getLevelPK().getId());
                    }
                }
                return o2.getKesAmount().compareTo(o1.getKesAmount());
            }
        });
        return invoiceDetails;
    }

    public void setInvoiceDetails(List<InvoiceDetail> invoiceDetails) {
        if (invoiceDetails != null) {
            for (InvoiceDetail det : invoiceDetails) {
                this.invoiceDetails = invoiceDetails;
                det.setInvoice(this);
            }
        }
    }

    public Collection<RenewalInvoiceDetail> getRenewalInvoiceDetails() {
        return renewalInvoiceDetails;
    }

    public void setRenewalInvoiceDetails(Collection<RenewalInvoiceDetail> renewalInvoiceDetails) {
        this.renewalInvoiceDetails = renewalInvoiceDetails;
    }

    public StudentCourseSitting getStudentCourseSitting() {
        return studentCourseSitting;
    }

    public void setStudentCourseSitting(StudentCourseSitting studentCourseSitting) {
        studentCourseSitting.setInvoice(this);
        this.studentCourseSitting = studentCourseSitting;
    }

    public Exemption getExemption() {
        return exemption;
    }

    public void setExemption(Exemption exemption) {
        this.exemption = exemption;
    }

    public FeeCode getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(FeeCode feeCode) {
        this.feeCode = feeCode;
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

    public Collection<ExemptionInvoiceDetail> getExemptionInvoiceDetails() {
        return exemptionInvoiceDetails;
    }

    public void setExemptionInvoiceDetails(Collection<ExemptionInvoiceDetail> exemptionInvoiceDetails) {
        this.exemptionInvoiceDetails = exemptionInvoiceDetails;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getNetAmount() {
        if (getLocalAmount() != null) {
            netAmount = getLocalAmount().min(new BigDecimal("0"));
        }
        return netAmount;
    }

    //Helper
    public void addInvoiceDetail(InvoiceDetail invoiceDetail) {
        if (invoiceDetail != null) {
            if (invoiceDetails == null) {
                invoiceDetails = new LinkedList<>();
            }
            invoiceDetails.add(invoiceDetail);
            invoiceDetail.setInvoice(this);
        }
    }//Helper

    public void addInvoiceDetail(RenewalInvoiceDetail invoiceDetail) {
        if (invoiceDetail != null) {
            if (renewalInvoiceDetails == null) {
                renewalInvoiceDetails = new LinkedList<>();
            }
            renewalInvoiceDetails.add(invoiceDetail);
            invoiceDetail.setInvoice(this);
        }
    }//Helper

    public void addInvoiceDetail(ExemptionInvoiceDetail invoiceDetail) {
        if (invoiceDetail != null) {
            if (exemptionInvoiceDetails == null) {
                exemptionInvoiceDetails = new LinkedList<>();
            }
            exemptionInvoiceDetails.add(invoiceDetail);
            invoiceDetail.setInvoice(this);
        }
    }

    public BigDecimal getLocalAmount() {
        if (getStudentCourse() != null) {
            if (getStudentCourse().getStudent() != null) {
                if (getStudentCourse().getStudent().getCountryId() != null) {
                    switch (getStudentCourse().getStudent().getCountryId().getCode()) {
                        case "1":
                            localAmount = this.getKesTotal();
                            return localAmount;
                        default:
                            localAmount = this.getUsdTotal();
                            return localAmount;
                    }
                }
            }
        }
        return localAmount;
    }

    public Currency getLocalCurrency() {
        if (getStudentCourse() != null) {
            if (getStudentCourse().getStudent() != null) {
                if (getStudentCourse().getStudent().getCountryId() != null) {
                    switch (getStudentCourse().getStudent().getCountryId().getCode()) {
                        case "1":
                            localCurrency = Currency.KSH;
                            break;
                        default:
                            localCurrency = Currency.USD;
                            break;
                    }
                }
            }
        }
        return localCurrency;
    }

    public List<ExamCentre> getExamCentres() {
        return examCentres;
    }

    public void setExamCentres(List<ExamCentre> examCentres) {
        this.examCentres = examCentres;
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
