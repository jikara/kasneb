/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Objects;

/**
 *
 * @author jikara
 */
public class ElligibleLevel {

    private String name;
    private Collection<Paper> papers;
    @JsonIgnore
    private Level level;
    private Boolean optional = false;

    public ElligibleLevel() {
    }

    public ElligibleLevel(String name, Level level, Collection<Paper> papers) {
        this.name = name;
        this.level = level;
        this.papers = papers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Collection<Paper> getPapers() {
        return papers;
    }

    public void setPapers(Collection<Paper> papers) {
        this.papers = papers;
    }

    public Boolean getOptional() {
        return optional;
    }

    public void setOptional(Boolean optional) {
        this.optional = optional;
    }

    public void addPaper(Paper paper) {
        if (paper != null && paper.getLevel().getLevelPK().getId().equals(this.getLevel().getLevelPK().getId())) {
            this.papers.add(paper);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.level);
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
        final ElligibleLevel other = (ElligibleLevel) obj;
        if (!Objects.equals(this.level, other.level)) {
            return false;
        }
        return true;
    }
}
