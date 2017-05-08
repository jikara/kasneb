/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "course")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 20)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c")
    ,@NamedQuery(name = "Course.findById", query = "SELECT c FROM Course c WHERE c.id = :id")
    ,@NamedQuery(name = "Course.findByName", query = "SELECT c FROM Course c WHERE c.name = :name")})
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "qualification")    
    private Collection<StudentQualification> studentCourseQualifications;    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "qualification", fetch = FetchType.LAZY)    
    protected Collection<CourseExemption> courseExemptions;    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  
    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institutionId", referencedColumnName = "id")
    private Institution institution; 
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "courseTypeCode", referencedColumnName = "code")
    @ManyToOne(optional = false)
    private CourseType courseType;
    @ManyToMany(mappedBy = "qualifications")
    private List<Exemption> exemptions;

    public Course() {
    }

    public Course(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Course(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        try {
            int code = Integer.parseInt(id);
            if (code < 10) {
                id = "0" + code;
            }
        } catch (NumberFormatException e) {

        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<CourseExemption> getCourseExemptions() {
        return courseExemptions;
    }

    public void setCourseExemptions(Collection<CourseExemption> courseExemptions) {
        this.courseExemptions = courseExemptions;
    }

    public Collection<StudentQualification> getStudentCourseQualifications() {
        return studentCourseQualifications;
    }

    public void setStudentCourseQualifications(Collection<StudentQualification> studentCourseQualifications) {
        this.studentCourseQualifications = studentCourseQualifications;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public CourseType getCourseType() {
        return courseType;
    }

    public void setCourseType(CourseType courseType) {
        this.courseType = courseType;
    }

    public List<Exemption> getExemptions() {
        return exemptions;
    }

    public void setExemptions(List<Exemption> exemptions) {
        this.exemptions = exemptions;
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
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "com.kasneb.entity.Course[ id=" + id + " ]";
    }

}
