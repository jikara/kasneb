/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Collection;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("OTHER")
public class OtherCourse extends Course {

    @JoinColumn(name = "courseTypeCode", referencedColumnName = "code", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference
    private OtherCourseType otherCourseType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qualificationId", referencedColumnName = "id")
    @JsonBackReference
    private OtherQualification otherQualification;

    public OtherCourse() {
        super();
    }

    @Override
    public void setName(String name) {
        super.setName(name); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return super.getName(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setId(String id) {
        super.setId(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getId() {
        return super.getId(); //To change body of generated methods, choose Tools | Templates.
    }

    public OtherQualification getOtherQualification() {
        return otherQualification;
    }

    public void setOtherQualification(OtherQualification otherQualification) {
        this.otherQualification = otherQualification;
    }

    public OtherCourseType getOtherCourseType() {
        return otherCourseType;
    }

    public void setOtherCourseType(OtherCourseType otherCourseType) {
        this.otherCourseType = otherCourseType;
    }

    @Override
    public void setCourseExemptions(Collection<CourseExemption> courseExemptions) {
        super.setCourseExemptions(courseExemptions); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<CourseExemption> getCourseExemptions() {
        return super.getCourseExemptions(); //To change body of generated methods, choose Tools | Templates.
    }

}
