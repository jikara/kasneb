/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity.pk;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jikara
 */
@Embeddable
public class PartPK implements Serializable {

    @Column(name = "id", insertable = false, updatable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "courseId", insertable = false, updatable = false)
    private String courseId;

    public PartPK() {
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
    public String toString() {
        return "PartPK{" + "id=" + id + ", courseId=" + courseId + '}';
    }
}
