/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import java.math.BigDecimal;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue("EXAM_ENTRY")
public class ExamInvoiceDetail extends InvoiceDetail {

    @ManyToOne
    @JoinColumn(name = "paperCode", referencedColumnName = "code")
    private Paper paper;
    @ManyToOne
    @JoinColumn(name = "courseId", referencedColumnName = "id")
    private Course course;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "sectionId", referencedColumnName = "id",insertable = false, updatable = false),
        @JoinColumn(name = "partId", referencedColumnName = "partId",insertable = false, updatable = false),
        @JoinColumn(name = "courseId", referencedColumnName = "courseId",insertable = false, updatable = false)
    })
    private Section section;
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "levelId", referencedColumnName = "id",insertable = false, updatable = false),
        @JoinColumn(name = "courseId", referencedColumnName = "courseId",insertable = false, updatable = false)
    })
    private Level level;

    public ExamInvoiceDetail() {
    }

    public ExamInvoiceDetail(Paper paper, Section section, Level level, BigDecimal kesAmount, BigDecimal usdAmount, BigDecimal gbpAmount, String description) {
        super(kesAmount, usdAmount, gbpAmount, description);
        this.paper = paper;
        this.section = section;
        this.level = level;
    }

    public void setCourse(Course course) {
        if (this.getLevel() != null) {
            course = getLevel().getCourse();
        }
        if (this.getSection().getPart() != null) {
            course = getSection().getPart().getCourse();
        }
        this.course = course;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

}
