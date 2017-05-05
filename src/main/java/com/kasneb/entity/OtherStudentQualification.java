/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.kasneb.entity.pk.StudentQualificationPK;
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
public class OtherStudentQualification extends StudentQualification {

    @ManyToOne
    @JoinColumn(name = "institutionId", referencedColumnName = "id")
    private Institution institution;
    @Column(name = "institutionName")
    private String institutionName;
    @Column(name = "courseName")
    private String courseName;
    @OneToMany(mappedBy = "otherStudentQualification", cascade = CascadeType.ALL)
    Collection<QualificationDocument> documents;
    @Transient
    private Integer codeType;

    @Override
    public StudentQualificationPK getStudentQualificationPK() {
        return super.getStudentQualificationPK(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setStudentQualificationPK(StudentQualificationPK studentQualificationPK) {
        super.setStudentQualificationPK(studentQualificationPK); //To change body of generated methods, choose Tools | Templates.
    }

    public OtherStudentQualification() {
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
        type = "Other";
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }
}
