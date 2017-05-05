/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity.pk;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jikara
 */
@Embeddable
public class SectionPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "partId")
    private Integer partId;
    @Basic(optional = false)
    @Column(name = "courseId")
    private String courseId;

    public SectionPK() {
    }

    public SectionPK(Integer id, Integer partId, String courseId) {
        this.id = id;
        this.partId = partId;
        this.courseId = courseId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPartId() {
        return partId;
    }

    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
        hash = 11 * hash + Objects.hashCode(this.partId);
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
        final SectionPK other = (SectionPK) obj;
        if (Objects.equals(this.id, other.id) && Objects.equals(this.partId, other.partId)) {
            return true;
        }
        return false;
    }

}
