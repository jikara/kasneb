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
public class PartPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "id", updatable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "courseId", updatable = false)
    private String courseId;

    public PartPK() {
    }

    public PartPK(Integer id) {
        this.id = id;
    }

    public PartPK(Integer id, String courseId) {
        this.id = id;
        this.courseId = courseId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.id);
        hash = 11 * hash + Objects.hashCode(this.courseId);
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
        final PartPK other = (PartPK) obj;
        return (Objects.equals(this.courseId, other.courseId) && Objects.equals(this.id, other.id));
    }
}
