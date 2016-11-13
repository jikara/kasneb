/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity.pk;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author jikara
 */
@Embeddable
public class SectionPK implements Serializable {

    @Column(name = "id", insertable = false, updatable = false)
    private Integer id;
    @Column(name = "partId", insertable = false, updatable = false)
    private Integer partId;
    @Column(name = "courseId", insertable = false, updatable = false)
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

}
