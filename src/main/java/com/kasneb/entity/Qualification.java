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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "qualification")
public class Qualification implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "description", nullable = false)
    private String description;
    @ManyToOne(optional = false)
    @JoinColumn(name = "type", referencedColumnName = "code", nullable = false)
    @JsonManagedReference
    private QualificationType type;
    @OneToMany(mappedBy = "qualification")
    @JsonBackReference
    private Collection<CourseExemption> courseExemptions;
    @OneToMany(mappedBy = "qualification")
    @JsonBackReference
    private Collection<StudentCourseQualification> studentCourses;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public QualificationType getType() {
        return type;
    }

    public void setType(QualificationType type) {
        this.type = type;
    }

    public Collection<CourseExemption> getCourseExemptions() {
        return courseExemptions;
    }

    public void setCourseExemptions(Collection<CourseExemption> courseExemptions) {
        this.courseExemptions = courseExemptions;
    }

    public Collection<StudentCourseQualification> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(Collection<StudentCourseQualification> studentCourses) {
        this.studentCourses = studentCourses;
    }

}
