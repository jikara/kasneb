/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.dto;

import com.kasneb.entity.Paper;
import java.util.List;

/**
 *
 * @author jikara
 */
public class CourseExemption {

    private String courseCode;
    private List<Paper> papers;

    public CourseExemption() {
    }

    public CourseExemption(String courseCode, List<Paper> papers) {
        this.courseCode = courseCode;
        this.papers = papers;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public List<Paper> getPapers() {
        return papers;
    }

    public void setPapers(List<Paper> papers) {
        this.papers = papers;
    }
}
