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
@Table(name = "guide")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Guide.findAll", query = "SELECT g FROM Guide g"),
    @NamedQuery(name = "Guide.findByDescription", query = "SELECT g FROM Guide g WHERE g.description = :description")})
public class Guide implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

    public Guide() {
    }

    public Guide(String description) {
        this.description = description;
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
        hash += (description != null ? description.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Guide)) {
            return false;
        }
        final Guide other = (Guide) object;
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        return Objects.equals(this.description, other.description);
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.Guide[ description=" + description + " ]";
    }

}
