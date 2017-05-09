/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kasneb.entity.pk.CourseExemptionPK;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "courseExemption")
public class CourseExemption implements Serializable {

    @EmbeddedId
    private CourseExemptionPK courseExemptionPK;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "qualificationId", referencedColumnName = "id", insertable = false, updatable = false)
    private Course qualification;
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne
    @JoinColumn(name = "courseId", referencedColumnName = "id", insertable = false, updatable = false)
    private KasnebCourse course;
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne
    @JoinColumn(name = "paperCode", referencedColumnName = "code", insertable = false, updatable = false)
    private Paper paper;

    public CourseExemption() {
    }

    public CourseExemption(CourseExemptionPK courseExemptionPK) {
        this.courseExemptionPK = courseExemptionPK;
    }

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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.courseExemptionPK);
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
        final CourseExemption other = (CourseExemption) obj;
        return Objects.equals(this.courseExemptionPK, other.courseExemptionPK);
    }
}
