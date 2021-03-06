/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "fee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fee.findAll", query = "SELECT f FROM Fee f")
    ,    @NamedQuery(name = "Fee.findById", query = "SELECT f FROM Fee f WHERE f.id = :id")
    ,    @NamedQuery(name = "FeeType.findByName", query = "SELECT f FROM Fee f WHERE f.name = :name")})
public class Fee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(max = 45)
    @Column(name = "name", nullable = false)
    private String name;
    @Basic
    @Column(name = "gbpAmount")
    private BigDecimal gbpAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "kesAmount")
    private BigDecimal kesAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "usdAmount")
    private BigDecimal usdAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    @Column(name = "effectiveDate")
    @Temporal(TemporalType.DATE)
    private Date effectiveDate;

    public Fee() {
    }

    public Fee(Integer id, String name, BigDecimal gbpAmount, BigDecimal kesAmount, BigDecimal usdAmount, Date effectiveDate, FeeTypeCode feeTypeCode, FeeCode feeCode, KasnebCourse course, CourseType courseType, Level level, Part part, Section section, Paper paper) {
        this.id = id;
        this.name = name;
        this.gbpAmount = gbpAmount;
        this.kesAmount = kesAmount;
        this.usdAmount = usdAmount;
        this.effectiveDate = effectiveDate;
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
        this.name = name;
    }

    public BigDecimal getGbpAmount() {
        return gbpAmount;
    }

    public void setGbpAmount(BigDecimal gbpAmount) {
        this.gbpAmount = gbpAmount;
    }

    public BigDecimal getKesAmount() {
        return kesAmount;
    }

    public void setKesAmount(BigDecimal kesAmount) {
        this.kesAmount = kesAmount;
    }

    public BigDecimal getUsdAmount() {
        return usdAmount;
    }

    public void setUsdAmount(BigDecimal usdAmount) {
        this.usdAmount = usdAmount;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.name);
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
        final Fee other = (Fee) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        return "Fee{" + "id=" + id + ", name=" + name + '}';
    }

}
