/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.math.BigDecimal;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("EXEMPTION")
public class ExemptionInvoiceDetail extends InvoiceDetail {

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "exemptionId", referencedColumnName = "exemptionId")
        ,@JoinColumn(name = "paperCode", referencedColumnName = "paperCode")
    })
    private ExemptionPaper paper;

    public ExemptionInvoiceDetail() {
    }

    public ExemptionInvoiceDetail(ExemptionPaper paper, BigDecimal kesAmount, BigDecimal usdAmount, BigDecimal gbpAmount, String description) {
        super(kesAmount, usdAmount, gbpAmount, description);
        this.paper = paper;
    }

    public ExemptionPaper getPaper() {
        return paper;
    }

    public void setPaper(ExemptionPaper paper) {
        this.paper = paper;
    }
}
