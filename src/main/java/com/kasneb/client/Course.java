/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.client;

import java.io.Serializable;

/**
 *
 * @author jikara
 */
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private Fee fee;
    private String id;
    private String name;

    public Course() {
    }

    public Course(String id) {
        this.id = id;
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

}
