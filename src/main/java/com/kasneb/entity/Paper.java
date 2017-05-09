/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
    @NamedQuery(name = "Paper.findAll", query = "SELECT p FROM Paper p")
    ,    @NamedQuery(name = "Paper.findByCode", query = "SELECT p FROM Paper p WHERE p.code = :code")
    ,    @NamedQuery(name = "Paper.findByName", query = "SELECT p FROM Paper p WHERE p.name = :name")})
public class Paper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "name", nullable = false)
    private String name;
    @ManyToMany(mappedBy = "elligiblePapers")
    private Collection<StudentCourse> studentCourses;
    @MapsId("id")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "sectionId", referencedColumnName = "id")
        ,@JoinColumn(name = "partId", referencedColumnName = "partId")
        ,@JoinColumn(name = "courseId", referencedColumnName = "courseId")
    })
    private Section section;
    @MapsId("id")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "levelId", referencedColumnName = "id")
        ,@JoinColumn(name = "courseId", referencedColumnName = "courseId")
    })
    private Level level;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paper")
    private Collection<StudentCourseSittingPaper> studentCourseSittingPapers;
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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public KasnebCourse getCourse() {
        return course;
    }

    public void setCourse(KasnebCourse course) {
        this.course = course;
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
