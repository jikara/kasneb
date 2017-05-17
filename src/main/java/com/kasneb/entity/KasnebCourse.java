/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import javax.persistence.Transient;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("KASNEB")
@NamedQueries({
    @NamedQuery(name = "KasnebCourse.findAll", query = "SELECT c FROM KasnebCourse c")
    ,@NamedQuery(name = "KasnebCourse.findById", query = "SELECT c FROM KasnebCourse c WHERE c.id = :id")
    ,@NamedQuery(name = "KasnebCourse.findByCourseType", query = "SELECT c FROM KasnebCourse c WHERE c.kasnebCourseType = :courseType")})
public class KasnebCourse extends Course {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "courseTypeCode", referencedColumnName = "code", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KasnebCourseType kasnebCourseType;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.LAZY)
    private Collection<Level> levelCollection;
    @JsonInclude
    private transient Integer courseTypeCode;
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Paper> papers;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "courseRequirement",
            joinColumns = {
                @JoinColumn(name = "courseId", referencedColumnName = "id", nullable = true)},
            inverseJoinColumns
            = @JoinColumn(name = "requirementId", referencedColumnName = "id", nullable = false))
    private Collection<Requirement> requirements;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.LAZY)
    private Collection<StudentCourse> studentCourses;
    @OneToMany(mappedBy = "course")
    private Collection<Part> parts;
    @Transient
    private Collection kasnebCourseExemptions;

    public KasnebCourse() {
        super();
    }

    public KasnebCourse(String id, String name) {
        super(id, name);
    }

    public KasnebCourse(String code) {
        super(code);
    }

    public Collection<Level> getLevelCollection() {
        return levelCollection;
    }

    public void setLevelCollection(Collection<Level> levelCollection) {
        this.levelCollection = levelCollection;
    }

    public Collection<Paper> getPapers() {
        return papers;
    }

    public void setPapers(Collection<Paper> papers) {
        if (papers != null) {
            for (Paper p : papers) {
                p.setCourse(this);
            }

        }
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
        if (parts != null) {
            for (Part p : parts) {
                p.setCourse(this);
            }
        }
        this.parts = parts;
    }

    public KasnebCourseType getKasnebCourseType() {
        return kasnebCourseType;
    }

    public void setKasnebCourseType(KasnebCourseType kasnebCourseType) {
        this.kasnebCourseType = kasnebCourseType;
    }

    public Integer getCourseTypeCode() {
        if (getKasnebCourseType() != null) {
            courseTypeCode = getKasnebCourseType().getCode();
            return courseTypeCode;
        }
        return courseTypeCode;
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
