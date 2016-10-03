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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "course")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c"),
    @NamedQuery(name = "Course.findById", query = "SELECT c FROM Course c WHERE c.id = :id"),
    @NamedQuery(name = "Course.findByCourseType", query = "SELECT c FROM Course c WHERE c.courseTypeCode.code=:courseTypeCode"),
    @NamedQuery(name = "Course.findByName", query = "SELECT c FROM Course c WHERE c.name = :name")})
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    @JsonManagedReference
    private Collection<Level> levelCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    @JsonManagedReference
    private Collection<Fee> feeTypeCollection;
    @JoinColumn(name = "courseTypeCode", referencedColumnName = "code")
    @ManyToOne(optional = false)
    @JsonBackReference
    private CourseType courseTypeCode;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    @JsonBackReference
    private Collection<StudentCourse> studentCourseCollection;
    @Transient
    private Integer courseType;
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
    @OneToMany(mappedBy = "course")
    @JsonManagedReference
    private Collection<CourseExemption> courseExemptions;

    public Course() {
    }

    public Course(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public CourseType getCourseTypeCode() {
        return courseTypeCode;
    }

    public void setCourseTypeCode(CourseType courseTypeCode) {
        this.courseTypeCode = courseTypeCode;
    }

    @XmlTransient
    public Collection<StudentCourse> getStudentCourseCollection() {
        return studentCourseCollection;
    }

    public void setStudentCourseCollection(Collection<StudentCourse> studentCourseCollection) {
        this.studentCourseCollection = studentCourseCollection;
    }

    public Integer getCourseType() {
        return this.courseTypeCode.getCode();
    }

    public void setCourseType(Integer courseType) {
        this.courseType = courseType;
    }

    public Collection<Paper> getPapers() {
        return papers;
    }

    public void setPapers(Collection<Paper> papers) {
        this.papers = papers;
    }

    public Collection<ExamCentre> getExamCentres() {
        return examCentres;
    }

    public void setExamCentres(Collection<ExamCentre> examCentres) {
        this.examCentres = examCentres;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.Course[ id=" + id + " ]";
    }

}
