/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.dto;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author jikara
 */
public class QualificationExemption {

    private String id;
    private String name;
    private Integer institutionId;
    private String institutionName;
    private Set<ExemptionCourse> exemptionCourses = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Set<ExemptionCourse> getExemptionCourses() {
        return exemptionCourses;
    }

    public void setExemptionCourses(Set<ExemptionCourse> exemptionCourses) {
        this.exemptionCourses = exemptionCourses;
    }

    public void addExemptionCourse(ExemptionCourse exemptionCourse) {
        this.exemptionCourses.add(exemptionCourse);
    }
}
