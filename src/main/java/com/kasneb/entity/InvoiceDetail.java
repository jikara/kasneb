/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;
import javax.persistence.Basic;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "invoiceDetail")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 20)
@DiscriminatorValue(value = "DEFAULT")
public class InvoiceDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kesAmount", nullable = false)
    private BigDecimal kesAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usdAmount", nullable = false)
    private BigDecimal usdAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "gbpAmount", nullable = false)
    private BigDecimal gbpAmount;
    @Basic(optional = false)
    @Column(name = "description", nullable = false)
    private String description;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceId", referencedColumnName = "id", nullable = false)
    private Invoice invoice;
    @JsonInclude
    private transient BigDecimal localAmount;
    @JsonInclude
    private transient Currency localCurrency;
    @Transient
    private String amountString;

    public InvoiceDetail() {
    }

    public InvoiceDetail(BigDecimal localAmount, String description) {
        this.localAmount = localAmount;
        this.description = description;
    }

    public InvoiceDetail(BigDecimal kesAmount, BigDecimal usdAmount, BigDecimal gbpAmount, String description) {
        this.kesAmount = kesAmount;
        this.usdAmount = usdAmount;
        this.gbpAmount = gbpAmount;
        this.description = description;
    }

    public InvoiceDetail(Invoice invoice) {
        this.invoice = invoice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getKesAmount() {
        return kesAmount;
    }

    public void setKesAmount(BigDecimal kesAmount) {
        this.kesAmount = kesAmount;
    }

    public BigDecimal getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(BigDecimal usdAmount) {
        this.usdAmount = usdAmount;
    }

    public BigDecimal getGbpAmount() {
        return gbpAmount;
    }

    public void setGbpAmount(BigDecimal gbpAmount) {
        this.gbpAmount = gbpAmount;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public BigDecimal getLocalAmount() {
        if (getLocalCurrency() != null) {
            switch (getLocalCurrency()) {
                case KSH:
                    localAmount = this.getKesAmount();
                    break;
                default:
                    localAmount = this.getUsdAmount();
                    break;
            }
        }
        return localAmount;
    }

    public Currency getLocalCurrency() {
        if (getInvoice() != null) {
            localCurrency = getInvoice().getLocalCurrency();
        }
        return localCurrency;
    }

    public String getAmountString() {
        if (getLocalAmount() != null) {
            DecimalFormat df = new DecimalFormat("#,###");
            amountString = df.format(getLocalAmount());
        }
        return amountString;
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
        if (!(object instanceof InvoiceDetail)) {
            return false;
        }
        InvoiceDetail other = (InvoiceDetail) object;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.InvoiceDetail[ id=" + id + " ]";
    }

}
