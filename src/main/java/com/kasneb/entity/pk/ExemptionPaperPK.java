/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity.pk;

import java.io.Serializable;
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
}
