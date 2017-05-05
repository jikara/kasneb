/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kasneb.entity.pk.PartPK;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "part")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Part.findAll", query = "SELECT p FROM Part p")})
public class Part implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private PartPK partPK;
    @Transient
    private String name;
    @JoinColumn(name = "courseId", referencedColumnName = "id",insertable=false,updatable=false)
    @ManyToOne(optional = false)
    @JsonBackReference
    private KasnebCourse course;
    @OneToMany(mappedBy = "part")
    @JsonManagedReference
    private Collection<Section> sectionCollection;
    @OneToMany(mappedBy = "part")
    private Collection<Paper> paperCollection;
    @OneToMany(mappedBy = "part")
    @JsonBackReference
    private Collection<Fee> feeTypes;

    public Part() {
    }

    public Part(PartPK partPK) {
        this.partPK = partPK;
    }

    public PartPK getPartPK() {
        return partPK;
    }

    public void setPartPK(PartPK partPK) {
        this.partPK = partPK;
    }

    public String getName() {
        String roman = "I";
        switch (getPartPK().getId()) {
            case 1:
                roman = "I";
                break;
            case 2:
                roman = "II";
                break;
            case 3:
                roman = "III";
                break;
        }
        name = "Part " + roman;
        return name;
    }

    public KasnebCourse getCourse() {
        return course;
    }

    public void setCourse(KasnebCourse course) {
        this.course = course;
    }

    public Collection<Section> getSectionCollection() {
        return sectionCollection;
    }

    public void setSectionCollection(Collection<Section> sectionCollection) {
        this.sectionCollection = sectionCollection;
    }

    public Collection<Paper> getPaperCollection() {
        return paperCollection;
    }

    public void setPaperCollection(Collection<Paper> paperCollection) {
        this.paperCollection = paperCollection;
    }

    public Collection<Fee> getFeeTypes() {
        return feeTypes;
    }

    public void setFeeTypes(Collection<Fee> feeTypes) {
        this.feeTypes = feeTypes;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.partPK);
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
        final Part other = (Part) obj;
        return Objects.equals(this.partPK, other.partPK);
    }

    @Override
    public String toString() {
        return "Part{" + "id=" + partPK + ", course=" + course + '}';
    }

}
