/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author jikara
 */
@Embeddable
public class FeeTypePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "feeTypeCode")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feeCode")
    private String feeCode;

    public FeeTypePK() {
    }

    public FeeTypePK(String code, String feeCode) {
        this.code = code;
        this.feeCode = feeCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.code);
        hash = 41 * hash + Objects.hashCode(this.feeCode);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FeeTypePK other = (FeeTypePK) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return Objects.equals(this.feeCode, other.feeCode);
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.FeeTypePK[ id=" + code + ", feeCode=" + feeCode + " ]";
    }

}
