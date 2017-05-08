/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

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

    public Collection<OtherCourse> getCourses() {
        return courses;
    }

    public void setCourses(Collection<OtherCourse> courses) {
        this.courses = courses;
    }

}
