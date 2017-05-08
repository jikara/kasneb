/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "payment")
@NamedQueries({ 
    @NamedQuery(name = "Payment.getSummary", query = "SELECT p FROM Payment p WHERE p.invoice.status.status =:status ORDER BY p.paymentTimestamp DESC"),
    @NamedQuery(name = "Payment.findUnsynchronized", query = "SELECT p FROM Payment p WHERE p.newEntry=false ORDER by p.paymentTimestamp DESC"),
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p JOIN p.invoice i WHERE i.studentCourse.student =:student AND  p.paymentTimestamp BETWEEN :startDate AND :endDate ORDER by p.paymentTimestamp DESC"),
    @NamedQuery(name = "Payment.findByFeeCode", query = "SELECT p FROM Payment p WHERE p.invoice.status.status =:status AND p.invoice.feeCode =:code ORDER BY p.paymentTimestamp DESC"),
    @NamedQuery(name = "Payment.findByDateRange", query = "SELECT p FROM Payment p  WHERE p.invoice.status.status =:status AND p.paymentTimestamp BETWEEN :startDate AND :endDate ORDER BY p.paymentTimestamp DESC"),
    @NamedQuery(name = "Payment.findByStudentAndCode", query = "SELECT p FROM Payment p JOIN p.invoice i WHERE i.feeCode =:feeCode AND i.studentCourse.student =:student ORDER by p.paymentTimestamp DESC"),
    @NamedQuery(name = "Payment.findByStudent", query = "SELECT p FROM Payment p JOIN p.invoice i WHERE i.studentCourse.student =:student ORDER by p.paymentTimestamp DESC"),
    @NamedQuery(name = "Payment.findByCodeAndDate", query = "SELECT p FROM Payment p WHERE p.invoice.status.status =:status AND p.invoice.feeCode =:code AND p.paymentTimestamp BETWEEN :startDate AND :endDate ORDER BY p.paymentTimestamp DESC")})
@XmlRootElement
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "receiptNo")
    private String receiptNo;
    @Basic(optional = false)
    @Column(name = "totalAmount")
    private BigDecimal totalAmount;
    @Basic(optional = false)
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic(optional = false)
    @Column(name = "serviceFee")
    private BigDecimal serviceFee = new BigDecimal(0);
    @Size(max = 255)
    @Column(name = "currency")
    private String currency;
    @Column(name = "channel", nullable = false)
    private String channel = "WALLET";
    @Column(name = "reference")
    private String kasnebRef;
    @Column(name = "phoneNumber")
    private String phoneNumber;
    @Transient    
    private String pin;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Nairobi")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "paymentTimestamp", nullable = false, updatable = false)
    private Date paymentTimestamp;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id")
    private StudentCourse studentCourse;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceId", referencedColumnName = "id")
    private Invoice invoice;
    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<PaymentDetail> paymentDetails;
    @JsonInclude
    private transient String feeCode;
    @JsonInclude
    private transient Student student;
    @JsonInclude
    private transient String fullRegNo;
    @Column(name = "synchronized")
    private Boolean newEntry = false;
    @Transient
    private ExamCentre centre;

    public Payment() {
    }

    public Payment(String receiptNo, BigDecimal amount, String currency, String kasnebRef, Date paymentTimestamp) {
        this.receiptNo = receiptNo;
        this.amount = amount;
        this.currency = currency;
        this.kasnebRef = kasnebRef;
        this.paymentTimestamp = paymentTimestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAmount() {
        if (totalAmount != null) {
            amount = totalAmount.subtract(new BigDecimal(0));
        }
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.totalAmount = amount;
        this.amount = amount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Date getPaymentTimestamp() {
        return paymentTimestamp;
    }

    public void setPaymentTimestamp(Date paymentTimestamp) {
        this.paymentTimestamp = paymentTimestamp;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public String getKasnebRef() {
        return kasnebRef;
    }

    public void setKasnebRef(String kasnebRef) {
        this.kasnebRef = kasnebRef;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getFeeCode() {
        if (getInvoice() != null) {
            if (getInvoice().getFeeCode() != null) {
                feeCode = getInvoice().getFeeCode().getCode();
            }
        }
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public Boolean getNewEntry() {
        return newEntry;
    }

    public void setNewEntry(Boolean newEntry) {
        this.newEntry = newEntry;
    }

    public Student getStudent() {
        if (getStudentCourse() != null) {
            student = getStudentCourse().getStudentObj();
        }
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Collection<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(Collection<PaymentDetail> paymentDetails) {
        for (PaymentDetail p : paymentDetails) {
            p.setPayment(this);
        }
        this.paymentDetails = paymentDetails;
    }

    public String getFullRegNo() {
        if (getStudentCourse() != null) {
            fullRegNo = getStudentCourse().getFullRegistrationNumber();
        }
        return fullRegNo;
    }

    public ExamCentre getCentre() {
        return centre;
    }

    public void setCentre(ExamCentre centre) {
        this.centre = centre;
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
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.Payment[ id=" + id + " ]";
    }

}
