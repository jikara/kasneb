/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "sittingPaper")
public class SittingPaper implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sittingId", referencedColumnName = "id", nullable = false)
    private Sitting sitting;
    @ManyToOne(optional = false)
    @JoinColumn(name = "paperCode", referencedColumnName = "code", nullable = false)
    private Paper paper;
    @Basic(optional = false)
    @Column(name = "sDate", nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sessiodId", referencedColumnName = "id", nullable = false)
    private SittingSession sesion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SittingPaper)) {
            return false;
        }
        SittingPaper other = (SittingPaper) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.SittingPaper[ id=" + id + " ]";
    }

}
