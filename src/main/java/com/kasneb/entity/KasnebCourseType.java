/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("KASNEB")
public class KasnebCourseType extends CourseType {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "courseType", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Collection<KasnebCourse> courseCollection;
    @OneToMany(mappedBy = "courseType")
    @JsonBackReference
    private Collection<Fee> feeTypes;
    @OneToMany(mappedBy = "courseType")
    private Collection<Requirement> courseRequirements;
    @ManyToOne
    @JoinColumn(name = "qualificationId", referencedColumnName = "id")
    @JsonIgnore
    private KasnebQualification qualification;

    @Override
    public Integer getCode() {
        return super.getCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCode(Integer code) {
        super.setCode(code); //To change body of generated methods, choose Tools | Templates.
    }

    public Collection<KasnebCourse> getCourseCollection() {
        return courseCollection;
    }

    public void setCourseCollection(Collection<KasnebCourse> courseCollection) {
        this.courseCollection = courseCollection;
    }

    public Collection<Fee> getFeeTypes() {
        return feeTypes;
    }

    public void setFeeTypes(Collection<Fee> feeTypes) {
        this.feeTypes = feeTypes;
    }

    public Collection<Requirement> getCourseRequirements() {
        return courseRequirements;
    }

    public void setCourseRequirements(Collection<Requirement> courseRequirements) {
        this.courseRequirements = courseRequirements;
    }

    public KasnebQualification getQualification() {
        return qualification;
    }

    public void setQualification(KasnebQualification qualification) {
        this.qualification = qualification;
    }
}
