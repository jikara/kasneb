/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@JsonInclude(Include.NON_NULL)
public class Paper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "code", nullable = false)
    protected String code;
    @Column(name = "name", nullable = false)
    private String name;
    @JsonIgnore
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "levelId", referencedColumnName = "id"),
        @JoinColumn(name = "courseId", referencedColumnName = "courseId", insertable = false, updatable = false)
    })
    private Level level;
    @JsonIgnore
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "partId", referencedColumnName = "id"),
        @JoinColumn(name = "courseId", referencedColumnName = "courseId", insertable = false, updatable = false)
    })
    private Part part;
    @JsonIgnore
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "sectionId", referencedColumnName = "id"),
        @JoinColumn(name = "partId", referencedColumnName = "id", insertable = false, updatable = false),
        @JoinColumn(name = "courseId", referencedColumnName = "courseId", insertable = false, updatable = false)
    })
    private Section section;
    @OneToOne(mappedBy = "paper")
    @JsonBackReference
    private StudentCourseSittingPaper studentCourseSittingPaper;
    @OneToMany(mappedBy = "paper")
    @JsonBackReference
    private Collection<Fee> feeTypes;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "courseId", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    @JsonIgnore
    private KasnebCourse course;
    @OneToMany(mappedBy = "paper")
    @JsonBackReference
    @JsonIgnore
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
