/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.dto;

import com.kasneb.entity.Paper;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author jikara
 */
public class ExemptionCourse {

    private String id;
    private String name;
    private Set<Paper> papers = new HashSet<>();

    public ExemptionCourse(String id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public Set<Paper> getPapers() {
        return papers;
    }

    public void setPapers(Set<Paper> papers) {
        this.papers = papers;
    }

    public void addPaper(Paper paper) {
        if (paper.getCourse().getId().equals(getId())) {
            this.papers.add(paper);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final ExemptionCourse other = (ExemptionCourse) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
