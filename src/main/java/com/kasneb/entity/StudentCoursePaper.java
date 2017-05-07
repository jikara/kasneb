/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kasneb.entity.pk.StudentCoursePaperPK;
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
@Table(name = "studentCoursePaper")
public class StudentCoursePaper implements Serializable {

    @EmbeddedId
    private StudentCoursePaperPK pk;
    @ManyToOne
    @JoinColumn(name = "studentCourseId", referencedColumnName = "id",insertable=false,updatable=false)
    private StudentCourse studentCourse;
    @ManyToOne
    @JoinColumn(name = "paperCode", referencedColumnName = "code",insertable=false,updatable=false)
    private Paper paper;

    public StudentCoursePaper() {
    }

    public StudentCoursePaper(StudentCourse studentCourse, Paper paper) {
        this.studentCourse = studentCourse;
        this.paper = paper;
    }

    public StudentCoursePaperPK getPk() {
        return pk;
    }

    public void setPk(StudentCoursePaperPK pk) {
        this.pk = pk;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public void setStudentCourse(StudentCourse studentCourse) {
        this.studentCourse = studentCourse;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

}
