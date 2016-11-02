/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue(value = "EXEMPTION")
public class ExemptionInvoice extends Invoice {

    @OneToMany(mappedBy = "invoice", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Collection<ExemptionInvoiceDetail> exemptionInvoiceDetails;
    @OneToMany(mappedBy = "exemptionInvoice")
    private Set<StudentCourseExemptionPaper> exemptionPapers;

    public ExemptionInvoice() {
        super();
    }

    public ExemptionInvoice(Set<StudentCourseExemptionPaper> exemptionPapers, String reference, Date dateGenerated) {
        super(reference, dateGenerated);
        this.exemptionPapers = exemptionPapers;
    }

    public Collection<ExemptionInvoiceDetail> getExemptionInvoiceDetails() {
        return exemptionInvoiceDetails;
    }

    public void setExemptionInvoiceDetails(Collection<ExemptionInvoiceDetail> exemptionInvoiceDetails) {
        this.exemptionInvoiceDetails = exemptionInvoiceDetails;
    }

    public Set<StudentCourseExemptionPaper> getExemptionPapers() {
        return exemptionPapers;
    }

    public void setExemptionPapers(Set<StudentCourseExemptionPaper> exemptionPapers) {
        this.exemptionPapers = exemptionPapers;
    }

    //Helper
    public void addExemptionInvoiceDetail(ExemptionInvoiceDetail invoiceDetail) {
        exemptionInvoiceDetails = null;
        if (invoiceDetail != null) {
            if (exemptionInvoiceDetails == null) {
                exemptionInvoiceDetails = new TreeSet<>();
            }
            exemptionInvoiceDetails.add(invoiceDetail);
            invoiceDetail.setInvoice(this);
        }
    }
}
