/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "level", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"name", "courseId"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Level.findAll", query = "SELECT l FROM Level l"),
    @NamedQuery(name = "Level.findByName", query = "SELECT l FROM Level l WHERE l.name = :name")})
public class Level implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private LevelPK levelPK;
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "courseId", referencedColumnName = "id")
    @ManyToOne(optional = false)
    @JsonBackReference
    private KasnebCourse course;
    @OneToMany(mappedBy = "level")
    //@JsonBackReference
    private Collection<Paper> paperCollection;
    @OneToMany(mappedBy = "level")
    @JsonBackReference
    private Collection<Fee> feeTypes;
    @OneToMany(mappedBy = "currentLevel")
    @JsonBackReference
    private Collection<StudentCourse> studentCourses;

    public Level() {
    }

    public Level(Integer id) {
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

    public KasnebCourse getCourse() {
        return course;
    }

    public void setCourse(KasnebCourse course) {
        this.course = course;
    }

    @XmlTransient
    public Collection<Paper> getPaperCollection() {
        return paperCollection;
    }

    public void setPaperCollection(Collection<Paper> paperCollection) {
        this.paperCollection = paperCollection;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.name);
        hash = 41 * hash + Objects.hashCode(this.course);
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
        final Level other = (Level) obj;
        return Objects.equals(this.id, other.id);
    }

}
