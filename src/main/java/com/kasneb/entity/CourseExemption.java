/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kasneb.entity.pk.CourseExemptionPK;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "courseExemption")
public class CourseExemption implements Serializable {

    @EmbeddedId
    //@JsonIgnore
    private CourseExemptionPK courseExemptionPK;
    @ManyToOne
    @JoinColumn(name = "qualificationId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonManagedReference
    @JsonIgnore
    private Course qualification;
    @ManyToOne
    @JoinColumn(name = "courseId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonManagedReference
    @JsonIgnore
    private KasnebCourse course;
    @ManyToOne
    @JoinColumn(name = "paperCode", referencedColumnName = "code", insertable = false, updatable = false)
    @JsonManagedReference
    private Paper paper;

    public CourseExemptionPK getCourseExemptionPK() {
        return courseExemptionPK;
    }

    public void setCourseExemptionPK(CourseExemptionPK courseExemptionPK) {
        this.courseExemptionPK = courseExemptionPK;
    }

    public Course getQualification() {
        return qualification;
    }

    public void setQualification(Course qualification) {
        this.qualification = qualification;
    }

    public KasnebCourse getCourse() {
        return course;
    }

    public void setCourse(KasnebCourse course) {
        this.course = course;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }
}
