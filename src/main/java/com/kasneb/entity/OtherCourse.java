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
import javax.persistence.Transient;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("OTHER")
public class OtherCourse extends Course {

    @ManyToOne
    @JoinColumn(name = "qualificationId", referencedColumnName = "id")
    @JsonBackReference
    @JsonIgnore
    private OtherQualification otherQualification;
    @Transient
    private Collection courseExemptions;
    @ManyToOne
    @JoinColumn(name = "institutionId", referencedColumnName = "id")
    @JsonBackReference
    private Institution institution;

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

    @Override
    public void setCourseExemptions(Collection<CourseExemption> courseExemptions) {
        super.setCourseExemptions(courseExemptions); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<CourseExemption> getCourseExemptions() {
        return super.getCourseExemptions(); //To change body of generated methods, choose Tools | Templates.
    }

}
