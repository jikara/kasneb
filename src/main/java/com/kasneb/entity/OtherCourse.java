/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("OTHER")
public class OtherCourse extends Course {

    @JoinColumn(name = "courseTypeCode", referencedColumnName = "code")
    @ManyToOne(optional = false)
    @JsonBackReference
    private OtherCourseType otherCourseType;
    @ManyToOne
    @JoinColumn(name = "qualificationId", referencedColumnName = "id")
    @JsonBackReference
    @JsonIgnore
    private OtherQualification otherQualification;
    @ManyToOne
    @JoinColumn(name = "institutionId", referencedColumnName = "id")
    @JsonBackReference
    private Institution institution;

    public OtherCourse() {
        super();
    }

    public OtherCourse(OtherCourseType otherCourseType, Collection courseExemptions, Institution institution) {
        this.otherCourseType = otherCourseType;
        super.courseExemptions = courseExemptions;
        this.institution = institution;
    }

    public OtherQualification getOtherQualification() {
        return otherQualification;
    }

    public void setOtherQualification(OtherQualification otherQualification) {
        this.otherQualification = otherQualification;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
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
