/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author jikara
 */
public class Section {

    Integer id;
    String name;
    Part part;

    public Section(Integer id) {
        this.id = id;
    }

    public Section(Integer id, Part part) {
        this.id = id;
        this.part = part;
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
        name = "Section " + id;
        this.name = name;
    }

    public Part getPart() {
        Integer partId = null;
        if (getId() != null) {
            switch (getId()) {
                case 1:
                    partId = 1;
                    break;
                case 2:
                    partId = 1;
                    break;
                case 3:
                    partId = 2;
                    break;
                case 4:
                    partId = 2;
                    break;
                case 5:
                    partId = 3;
                    break;
                case 6:
                    partId = 3;
                    break;
            }
        }
        return new Part(partId);
    }

    public void setPart(Part part) {
        this.part = part;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Section other = (Section) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}
