/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.kasneb.entity.pk.StudentCourseQualificationPK;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 *
 * @author jikara
 */
@Entity
@DiscriminatorValue(value = "OTHER")
public class OtherStudentCourseQualification extends StudentCourseQualification {

    @ManyToOne
    @JoinColumn(name = "institutionId", referencedColumnName = "id")
    private Institution institution;
    @Column(name = "institutionName")
    private String institutionName;
    @Column(name = "courseName")
    private String courseName;
    @OneToMany(mappedBy = "otherStudentCourseQualification", cascade = CascadeType.ALL)
    Collection<QualificationDocument> documents;
    @Transient
    private String type = "Other";
    @Transient
    private Integer codeType;

    @Override
    public StudentCourseQualificationPK getStudentCourseQualificationPK() {
        return super.getStudentCourseQualificationPK(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStudentCourseQualificationPK(StudentCourseQualificationPK studentCourseQualificationPK) {
        super.setStudentCourseQualificationPK(studentCourseQualificationPK); //To change body of generated methods, choose Tools | Templates.
    }

    public OtherStudentCourseQualification() {
        super();
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Collection<QualificationDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(Collection<QualificationDocument> documents) {
        this.documents = documents;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
