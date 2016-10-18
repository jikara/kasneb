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
import javax.persistence.OneToOne;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue(value = "EXEMPTION")
public class ExemptionInvoiceDetail extends InvoiceDetail {

    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "studentCourseId", referencedColumnName = "studentCourseId"),
        @JoinColumn(name = "paperCode", referencedColumnName = "paperCode")
    })
    private StudentCourseExemptionPaper studentCourseExemptionPaper;

    public ExemptionInvoiceDetail() {
        super();
    }

    public ExemptionInvoiceDetail(StudentCourseExemptionPaper studentCourseExemptionPaper, BigDecimal kesAmount, BigDecimal usdAmount, BigDecimal gbpAmount, String description) {
        super(kesAmount, usdAmount, gbpAmount, description);
        this.studentCourseExemptionPaper = studentCourseExemptionPaper;
    }

    public StudentCourseExemptionPaper getStudentCourseExemptionPaper() {
        return studentCourseExemptionPaper;
    }

    public void setStudentCourseExemptionPaper(StudentCourseExemptionPaper studentCourseExemptionPaper) {
        this.studentCourseExemptionPaper = studentCourseExemptionPaper;
    }

}
