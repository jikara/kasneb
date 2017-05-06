/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity.pk;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jikara
 */
@Embeddable
public class ExemptionPaperPK implements Serializable {

    @Column(name = "paperCode")
    private String paperCode;
    @Column(name = "exemptionId")
    private Integer exemptionId;

    public ExemptionPaperPK() {
    }

    public ExemptionPaperPK(String paperCode, Integer exemptionId) {
        this.paperCode = paperCode;
        this.exemptionId = exemptionId;
    }

    public String getPaperCode() {
        return paperCode;
    }

    public void setPaperCode(String paperCode) {
        this.paperCode = paperCode;
    }

    public Integer getExemptionId() {
        return exemptionId;
    }

    public void setExemptionId(Integer exemptionId) {
        this.exemptionId = exemptionId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.paperCode);
        hash = 89 * hash + Objects.hashCode(this.exemptionId);
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
        final ExemptionPaperPK other = (ExemptionPaperPK) obj;
        if (!Objects.equals(this.paperCode, other.paperCode)) {
            return false;
        }
        if (!Objects.equals(this.exemptionId, other.exemptionId)) {
            return false;
        }
        return true;
    }
}
