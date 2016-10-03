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
@Table(name = "centreRegion")
@XmlRootElement
public class CentreRegion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;
    @NotNull
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(name = "zoneId", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private CentreZone zone;
    @OneToMany(mappedBy = "region")
    @JsonManagedReference
    private Collection<CentreCluster> clusters;

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

    public CentreZone getZone() {
        return zone;
    }

    public void setZone(CentreZone zone) {
        this.zone = zone;
    }

    public Collection<CentreCluster> getClusters() {
        return clusters;
    }

    public void setClusters(Collection<CentreCluster> clusters) {
        this.clusters = clusters;
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
        if (!(object instanceof CentreRegion)) {
            return false;
        }
        CentreRegion other = (CentreRegion) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.CentreRegion[ id=" + id + " ]";
    }

}
