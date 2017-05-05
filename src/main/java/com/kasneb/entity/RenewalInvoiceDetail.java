/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue(value = "RENEWAL")
public class RenewalInvoiceDetail extends InvoiceDetail {

    @Column(name = "ryear")
    @Basic(optional = false)
    private Integer year;

    public RenewalInvoiceDetail() {
        super();
    }

    public RenewalInvoiceDetail(Integer year, BigDecimal kesAmount, BigDecimal usdAmount, BigDecimal gbpAmount, String description) {
        super(kesAmount, usdAmount, gbpAmount, description);
        this.year = year;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
