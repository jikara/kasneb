/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author jikara
 */
public class ElligibleSection {

    private String name;    
    private Section section;
    private Boolean optional = true;
    private List<Paper> papers;

    public ElligibleSection() {
    }

    public ElligibleSection(String name, Section section, List<Paper> papers, Boolean optional) {
        this.name = name;
        this.section = section;
        this.papers = papers;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Paper> getPapers() {
        Collections.sort(papers, new Comparator<Paper>() {
            @Override
            public int compare(Paper p1, Paper p2) {
                return p1.getCode().compareTo(p2.getCode());
            }
        });
        return papers;
    }

    public void setPapers(List<Paper> papers) {
        this.papers = papers;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    public void addPaper(Paper paper) {
        if (paper != null && paper.getSection().equals(this.getSection())) {
            this.papers.add(paper);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.name);
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
        final ElligibleSection other = (ElligibleSection) obj;
        if (!Objects.equals(this.section, other.section)) {
            return false;
        }
        return true;
    }
}
