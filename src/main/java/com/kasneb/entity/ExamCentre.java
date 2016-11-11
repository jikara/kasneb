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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "examCentre")
@XmlRootElement
public class ExamCentre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "code", nullable = false)
    private Integer code;
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    @Basic(optional = false)
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    @ManyToOne
    @JoinColumn(name = "zoneCode", referencedColumnName = "code")
    private CentreZone zone;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public CentreZone getZone() {
        return zone;
    }

    public void setZone(CentreZone zone) {
        this.zone = zone;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.code);
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
        final ExamCentre other = (ExamCentre) obj;
        return Objects.equals(this.code, other.code);
    }

    @Override
    public String toString() {
        return "ExamCentre{" + "code=" + code + ", name=" + name + '}';
    }

}
