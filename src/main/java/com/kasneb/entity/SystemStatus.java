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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "systemStatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SystemStatus.findAll", query = "SELECT s FROM SystemStatus s"),
    @NamedQuery(name = "SystemStatus.findByCode", query = "SELECT s FROM SystemStatus s WHERE s.code = :code"),
    @NamedQuery(name = "SystemStatus.findByDescription", query = "SELECT s FROM SystemStatus s WHERE s.description = :description")})
public class SystemStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "code",nullable=false)
    private Integer code;
    @Size(max = 45)
    @Column(name = "description",nullable=false)
    private String description;

    public SystemStatus() {
    }

    public SystemStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
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
        final SystemStatus other = (SystemStatus) obj;
        return Objects.equals(this.code, other.code);
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.SystemStatus[ code=" + code + " ]";
    }
    
}
