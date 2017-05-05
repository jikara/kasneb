/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import com.kasneb.entity.Course;
import java.util.List;

/**
 *
 * @author jikara
 */
public class Part {

    Integer id;
    String name;
    Course course;
    List<Section> sections;

    public Part(Integer id) {
        this.id = id;
    }

    public Part(Integer id, Course course) {
        this.id = id;
        this.course = course;
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
        name = "Part " + getId();
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
