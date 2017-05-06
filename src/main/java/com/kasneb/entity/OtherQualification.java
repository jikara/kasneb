/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Collection;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("OTHER")
public class OtherQualification extends Qualification {

    @OneToMany(mappedBy = "otherQualification")

    private Collection<OtherCourse> courses;
    @OneToMany(mappedBy = "qualification")

    private Collection<OtherCourseType> otherQualificationTypes;

    public Collection<OtherCourse> getCourses() {
        return courses;
    }

    public void setCourses(Collection<OtherCourse> courses) {
        this.courses = courses;
    }

    public Collection<OtherCourseType> getOtherQualificationTypes() {
        return otherQualificationTypes;
    }

    public void setOtherQualificationTypes(Collection<OtherCourseType> otherQualificationTypes) {
        this.otherQualificationTypes = otherQualificationTypes;
    }

}
