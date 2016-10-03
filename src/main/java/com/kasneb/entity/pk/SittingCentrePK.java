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
public class SittingCentrePK implements Serializable {

    @Column(name = "centreCode")
    private Integer centreCode;
    @Column(name = "centreId")
    private Integer sittingId;

    public Integer getCentreCode() {
        return centreCode;
    }

    public void setCentreCode(Integer centreCode) {
        this.centreCode = centreCode;
    }

    public Integer getSittingId() {
        return sittingId;
    }

    public void setSittingId(Integer sittingId) {
        this.sittingId = sittingId;
    }
}
