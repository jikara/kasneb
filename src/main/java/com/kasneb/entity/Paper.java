/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "paper")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paper.findAll", query = "SELECT p FROM Paper p"),
    @NamedQuery(name = "Paper.findByCode", query = "SELECT p FROM Paper p WHERE p.code = :code"),
    @NamedQuery(name = "Paper.findByName", query = "SELECT p FROM Paper p WHERE p.name = :name")})
public class Paper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "name", nullable = false)
    private String name;     
    @JoinTable(name = "studentCoursePaper", joinColumns = {
        @JoinColumn(name = "paperCode", referencedColumnName = "code")}, inverseJoinColumns = {
        @JoinColumn(name = "studentCourseId", referencedColumnName = "id")})
    @ManyToMany(fetch=FetchType.LAZY)
    private Collection<StudentCourse> studentCourses;   
    @ManyToOne
    @PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "levelId", referencedColumnName = "id"),
        @PrimaryKeyJoinColumn(name = "courseId", referencedColumnName = "courseId")
    })
    private Level level;    
    @ManyToOne
    @PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "partId", referencedColumnName = "id"),
        @PrimaryKeyJoinColumn(name = "courseId", referencedColumnName = "courseId")
    })
    private Part part;    
    @ManyToOne
    @PrimaryKeyJoinColumns({
        @PrimaryKeyJoinColumn(name = "sectionId", referencedColumnName = "id"),
        @PrimaryKeyJoinColumn(name = "partId", referencedColumnName = "partId"),
        @PrimaryKeyJoinColumn(name = "courseId", referencedColumnName = "courseId")
    })
    private Section section;    
    @OneToOne(mappedBy = "paper")
    private StudentCourseSittingPaper studentCourseSittingPaper;    
    @OneToMany(mappedBy = "paper")
    private Collection<Fee> feeTypes;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "courseId", referencedColumnName = "id", nullable = false)
    private KasnebCourse course;
    @OneToMany(mappedBy = "paper")
    private Collection<CourseExemption> courseExemptions;

    public Paper() {
    }

    public Paper(String code) {
        this.code = code;
    }

    public Paper(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public KasnebCourse getCourse() {
        return course;
    }

    public void setCourse(KasnebCourse course) {
        this.course = course;
    }

    public StudentCourseSittingPaper getStudentCourseSittingPaper() {
        return studentCourseSittingPaper;
    }

    public void setStudentCourseSittingPaper(StudentCourseSittingPaper studentCourseSittingPaper) {
        this.studentCourseSittingPaper = studentCourseSittingPaper;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.code);
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
        final Paper other = (Paper) obj;
        return Objects.equals(this.code, other.code);
    }

}
