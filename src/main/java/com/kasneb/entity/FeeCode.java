/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "feeCode")
@XmlTransient
public class FeeCode implements Serializable {

    @Id
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "feeCode",fetch = FetchType.LAZY)

    private Collection<Fee> feeTypes;
    @OneToMany(mappedBy = "feeCode",fetch = FetchType.LAZY)

    private Collection<Invoice> invoices;

    public FeeCode() {
    }

    public FeeCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Collection<Fee> getFeeTypes() {
        return feeTypes;
    }

    public void setFeeTypes(Collection<Fee> feeTypes) {
        this.feeTypes = feeTypes;
    }

    public Collection<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Collection<Invoice> invoices) {
        this.invoices = invoices;
    }

    @Override
    public String toString() {
        return "FeeCode{" + "code=" + code + ", feeTypes=" + feeTypes + ", invoices=" + invoices + '}';
    }

}
