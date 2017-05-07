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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
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
    ,
    @NamedQuery(name = "Fee.findById", query = "SELECT f FROM Fee f WHERE f.id = :id")
    ,
    @NamedQuery(name = "Fee.findByCode", query = "SELECT f FROM Fee f WHERE f.feeCode = :feeCode")
    ,
    @NamedQuery(name = "Fee.findByFeeCode", query = "SELECT f FROM Fee f WHERE f.feeTypeCode = :feeTypeCode")
    ,
    @NamedQuery(name = "FeeType.findByName", query = "SELECT f FROM Fee f WHERE f.name = :name")})
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
    @JoinColumn(name = "feeTypeCode", referencedColumnName = "code", nullable = false)
    @ManyToOne

    private FeeTypeCode feeTypeCode;
    @JoinColumn(name = "feeCode", referencedColumnName = "code", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)

    private FeeCode feeCode;
    @JoinColumn(name = "courseId", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private KasnebCourse course;
    @JoinColumn(name = "courseTypeCode", referencedColumnName = "code")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private CourseType courseType;
    @MapsId("id")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "sectionId", referencedColumnName = "id")
        ,@JoinColumn(name = "partId", referencedColumnName = "partId")
        ,@JoinColumn(name = "courseId", referencedColumnName = "courseId")
    })
    private Section section;
    @MapsId("id")
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "levelId", referencedColumnName = "id")
        ,@JoinColumn(name = "courseId", referencedColumnName = "courseId")
    })
    private Level level;
    @MapsId("id")
    @JoinColumns({
        @JoinColumn(name = "partId", referencedColumnName = "id")
        ,@JoinColumn(name = "courseId", referencedColumnName = "courseId")
    })
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Part part;
    @JoinColumn(name = "paperCode", referencedColumnName = "code")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)

    private Paper paper;

    public Fee() {
    }

    public Fee(Integer id, String name, BigDecimal gbpAmount, BigDecimal kesAmount, BigDecimal usdAmount, Date effectiveDate, FeeTypeCode feeTypeCode, FeeCode feeCode, KasnebCourse course, CourseType courseType, Level level, Part part, Section section, Paper paper) {
        this.id = id;
        this.name = name;
        this.gbpAmount = gbpAmount;
        this.kesAmount = kesAmount;
        this.usdAmount = usdAmount;
        this.effectiveDate = effectiveDate;
        this.feeTypeCode = feeTypeCode;
        this.feeCode = feeCode;
        this.course = course;
        this.courseType = courseType;
        this.level = level;
        this.part = part;
        this.section = section;
        this.paper = paper;
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

    public FeeTypeCode getFeeTypeCode() {
        return feeTypeCode;
    }

    public void setFeeTypeCode(FeeTypeCode feeTypeCode) {
        this.feeTypeCode = feeTypeCode;
    }

    public FeeCode getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(FeeCode feeCode) {
        this.feeCode = feeCode;
    }

    public KasnebCourse getCourse() {
        return course;
    }

    public void setCourse(KasnebCourse course) {
        this.course = course;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
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
