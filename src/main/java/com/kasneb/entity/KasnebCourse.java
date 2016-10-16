/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "kasnebCourse")
@DiscriminatorValue("KASNEB")
@NamedQueries({
    @NamedQuery(name = "KasnebCourse.findAll", query = "SELECT c FROM KasnebCourse c"),
    @NamedQuery(name = "KasnebCourse.findById", query = "SELECT c FROM KasnebCourse c WHERE c.id = :id"),
    @NamedQuery(name = "KasnebCourse.findByCourseType", query = "SELECT c FROM KasnebCourse c WHERE c.courseType = :courseType")})
public class KasnebCourse extends Course {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    @JsonManagedReference
    private Collection<Level> levelCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    @JsonManagedReference
    private Collection<Fee> feeTypeCollection;
    @JoinColumn(name = "courseTypeCode", referencedColumnName = "code")
    @ManyToOne(optional = false)
    @JsonBackReference
    private CourseType courseType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    @JsonBackReference
    private Collection<StudentCourse> studentCourseCollection;
    @Transient
    private Integer courseTypeCode;
    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Collection<Paper> papers;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "courseRequirement",
            joinColumns = {
                @JoinColumn(name = "courseId", referencedColumnName = "id", nullable = true)},
            inverseJoinColumns
            = @JoinColumn(name = "requirementId", referencedColumnName = "id", nullable = false))
    @JsonBackReference
    private Collection<Requirement> requirements;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    @JsonBackReference
    private Collection<StudentCourse> studentCourses;
    @OneToMany(mappedBy = "course")
    @JsonManagedReference
    private Collection<Part> parts;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "courseExamCentre",
            joinColumns = {
                @JoinColumn(name = "courseId", referencedColumnName = "id", nullable = true)},
            inverseJoinColumns
            = @JoinColumn(name = "centreId", referencedColumnName = "code", nullable = false))
    @JsonBackReference
    private Collection<ExamCentre> examCentres;
    @Transient
    private Collection courseExemptions;

    public KasnebCourse() {
        super();
    }

    public KasnebCourse(Integer code) {
        super(code);
    }

    public Collection<Level> getLevelCollection() {
        return levelCollection;
    }

    public void setLevelCollection(Collection<Level> levelCollection) {
        this.levelCollection = levelCollection;
    }

    public Collection<Fee> getFeeTypeCollection() {
        return feeTypeCollection;
    }

    public void setFeeTypeCollection(Collection<Fee> feeTypeCollection) {
        this.feeTypeCollection = feeTypeCollection;
    }

    public Collection<StudentCourse> getStudentCourseCollection() {
        return studentCourseCollection;
    }

    public void setStudentCourseCollection(Collection<StudentCourse> studentCourseCollection) {
        this.studentCourseCollection = studentCourseCollection;
    }

    public Collection<Paper> getPapers() {
        return papers;
    }

    public void setPapers(Collection<Paper> papers) {
        this.papers = papers;
    }

    public Collection<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(Collection<Requirement> requirements) {
        this.requirements = requirements;
    }

    public Collection<StudentCourse> getStudentCourses() {
        return studentCourses;
    }

    public void setStudentCourses(Collection<StudentCourse> studentCourses) {
        this.studentCourses = studentCourses;
    }

    public Collection<Part> getParts() {
        return parts;
    }

    public void setParts(Collection<Part> parts) {
        this.parts = parts;
    }

    public Collection<ExamCentre> getExamCentres() {
        return examCentres;
    }

    public void setExamCentres(Collection<ExamCentre> examCentres) {
        this.examCentres = examCentres;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public Integer getCourseTypeCode() {
        if (getCourseType() != null) {
            courseTypeCode = getCourseType().getCode();
        }
        return courseTypeCode;
    }

    public void setCourseTypeCode(Integer courseTypeCode) {
        this.courseTypeCode = courseTypeCode;
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