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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "courseType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CourseType.findAll", query = "SELECT c FROM CourseType c"),
    @NamedQuery(name = "CourseType.findByCode", query = "SELECT c FROM CourseType c WHERE c.code = :code"),
    @NamedQuery(name = "CourseType.findByName", query = "SELECT c FROM CourseType c WHERE c.name = :name")})
public class CourseType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Basic(optional = false)
    @Column(name = "code", nullable = false)
    private Integer code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name", nullable = false)
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "courseTypeCode", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Collection<Course> courseCollection;
    @OneToMany(mappedBy = "courseTypeCode")
    @JsonBackReference
    private Collection<Fee> feeTypes;
    @OneToMany(mappedBy = "courseType")
    private Collection<Requirement> courseRequirements;

    public CourseType() {
    }

    public CourseType(Integer code) {
        this.code = code;
    }

    public CourseType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

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

    @XmlTransient
    public Collection<Course> getCourseCollection() {
        return courseCollection;
    }

    public void setCourseCollection(Collection<Course> courseCollection) {
        this.courseCollection = courseCollection;
    }

    public Collection<Requirement> getCourseRequirements() {
        return courseRequirements;
    }

    public void setCourseRequirements(Collection<Requirement> courseRequirements) {
        this.courseRequirements = courseRequirements;
    }

    @XmlTransient
    public Collection<Fee> getFeeTypes() {
        return feeTypes;
    }

    public void setFeeTypes(Collection<Fee> feeTypes) {
        this.feeTypes = feeTypes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (code != null ? code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CourseType)) {
            return false;
        }
        CourseType other = (CourseType) object;

        return !((this.code == null && other.code != null) || (this.code != null && !this.code.equals(other.code)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.CourseType[ code=" + code + " ]";
    }

}
