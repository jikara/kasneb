/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "centreZone")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "zoneType")
public class CentreZone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "code")
    private String code;
    @Transient
    private String id;
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
    private Collection<ExamCentre> centres;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<ExamCentre> getCentres() {
        return centres;
    }

    public void setCentres(Collection<ExamCentre> centres) {
        if (centres != null) {
            for (ExamCentre c : centres) {
                c.setZone(this);
            }
        }
        this.centres = centres;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.code);
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
        final CentreZone other = (CentreZone) obj;
        if (!Objects.equals(this.code, other.code)) {
            return false;
        }
        return true;
    }

}
