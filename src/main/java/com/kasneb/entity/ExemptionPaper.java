/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kasneb.entity.pk.ExemptionPaperPK;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "exemptionPaper")
public class ExemptionPaper implements Serializable {

    @EmbeddedId
    private ExemptionPaperPK exemptionPaperPK;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exemptionId", referencedColumnName = "id",insertable = false, updatable = false)
    private Exemption exemption;
    @ManyToOne
    @JoinColumn(name = "paperCode", referencedColumnName = "code",insertable = false, updatable = false)
    private Paper paper;
    @Basic(optional = false)
    @Column(name = "verified")
    private Boolean verified = false;
    @Basic(optional = false)
    @Column(name = "paid")
    private Boolean paid = false;
    @Column(name = "created", updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date created;
    @Column(name = "dateVerified")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateVerified;
    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User verifiedBy;
    @Column(name = "verificationStatus")
    @Enumerated(EnumType.STRING)
    VerificationStatus verificationStatus = VerificationStatus.PENDING;
    @Lob
    @Column(name = "remarks")
    private String remarks;
    @Transient
    private boolean elligible;

    public ExemptionPaper() {
    }

    public ExemptionPaper(Paper paper, Date created, Boolean paid, Boolean verified, VerificationStatus verificationStatus) {
        this.paper = paper;
        this.created = created;
        this.paid = paid;
        this.verified = verified;
        this.verificationStatus = verificationStatus;
    }

    public ExemptionPaperPK getExemptionPaperPK() {
        return exemptionPaperPK;
    }

    public void setExemptionPaperPK(ExemptionPaperPK exemptionPaperPK) {
        this.exemptionPaperPK = exemptionPaperPK;
    }

    public Exemption getExemption() {
        return exemption;
    }

    public void setExemption(Exemption exemption) {
        this.exemption = exemption;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(Date dateVerified) {
        this.dateVerified = dateVerified;
    }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isElligible() {
        if (this.getVerificationStatus() != null && this.getPaid() != null) {
            return getVerificationStatus().equals(VerificationStatus.APPROVED) && getPaid();
        }
        return false;
    }
}
