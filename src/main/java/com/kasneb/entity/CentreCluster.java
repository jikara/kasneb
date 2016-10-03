/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "centreCluster")
@XmlRootElement
public class CentreCluster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotNull
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(name = "regionId", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CentreRegion region;
    @OneToMany(mappedBy = "clusterId")
    @JsonManagedReference
    private Collection<ExamCentre> examCentres;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CentreRegion getRegion() {
        return region;
    }

    public void setRegion(CentreRegion region) {
        this.region = region;
    }

    public Collection<ExamCentre> getExamCentres() {
        return examCentres;
    }

    public void setExamCentres(Collection<ExamCentre> examCentres) {
        this.examCentres = examCentres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CentreCluster)) {
            return false;
        }
        CentreCluster other = (CentreCluster) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.CentreCluster[ id=" + id + " ]";
    }

}
