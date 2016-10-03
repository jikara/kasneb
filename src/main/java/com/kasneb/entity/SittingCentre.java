/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kasneb.entity.pk.SittingCentrePK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "sittingCentre")
@XmlRootElement
public class SittingCentre implements Serializable {

    @EmbeddedId
    private SittingCentrePK sittingCentrePK;
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    @Column(name = "booked", nullable = false)
    private Integer booked;
    @Column(name = "remaining", nullable = false)
    private Integer remaining;
    @ManyToOne
    @JoinColumn(name = "sittingId", referencedColumnName = "id", nullable = false, updatable = false)
    @JsonManagedReference
    private Sitting sitting;
    @ManyToOne
    @JoinColumn(name = "centreCode", referencedColumnName = "code", nullable = false, insertable = false, updatable = false)
    @JsonManagedReference
    private ExamCentre centre;

    public SittingCentre() {
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getBooked() {
        return booked;
    }

    public void setBooked(Integer booked) {
        this.booked = booked;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public ExamCentre getCentre() {
        return centre;
    }

    public void setCentre(ExamCentre centre) {
        this.centre = centre;
    }

    public Sitting getSitting() {
        return sitting;
    }

    public void setSitting(Sitting sitting) {
        this.sitting = sitting;
    }

    public SittingCentrePK getSittingCentrePK() {
        return sittingCentrePK;
    }

    public void setSittingCentrePK(SittingCentrePK sittingCentrePK) {
        this.sittingCentrePK = sittingCentrePK;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.sittingCentrePK);
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
        final SittingCentre other = (SittingCentre) obj;
        return Objects.equals(this.sittingCentrePK, other.sittingCentrePK);
    }

}
