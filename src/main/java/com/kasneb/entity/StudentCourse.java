/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jikara
 */
@Entity
@Table(name = "studentCourse")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StudentCourse.findAll", query = "SELECT s FROM StudentCourse s"),
    @NamedQuery(name = "StudentCourse.findByRegistrationNumber", query = "SELECT s FROM StudentCourse s WHERE s.registrationNumber = :registrationNumber"),
    @NamedQuery(name = "StudentCourse.findByCreated", query = "SELECT s FROM StudentCourse s WHERE s.created = :created"),
    @NamedQuery(name = "StudentCourse.findByDocument", query = "SELECT s FROM StudentCourse s WHERE s.document = :document"),
    @NamedQuery(name = "StudentCourse.findByVerified", query = "SELECT s FROM StudentCourse s WHERE s.verified = :verified"),
    @NamedQuery(name = "StudentCourse.findByDateVerified", query = "SELECT s FROM StudentCourse s WHERE s.dateVerified = :dateVerified"),
    @NamedQuery(name = "StudentCourse.findByRemarks", query = "SELECT s FROM StudentCourse s WHERE s.remarks = :remarks")})
public class StudentCourse implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "registrationNumber")
    @Size(max = 45)
    private String registrationNumber;
    @Basic(optional = false)
    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created = new Date();
    @Column(name = "document")
    @Size(max = 45)
    private String document;
    @Basic(optional = false)
    @NotNull
    @Column(name = "active", nullable = false)
    private boolean active = false;
    @Basic(optional = false)
    @NotNull
    @Column(name = "verified", nullable = false)
    private boolean verified = false;
    @Column(name = "dateVerified")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateVerified;
    @ManyToOne(optional = true)
    @JoinColumn(name = "verifiedBy")
    @JsonManagedReference
    private User verifiedBy;
    @Column(name = "remarks")
    @Size(max = 200)
    private String remarks;
    @JoinColumn(name = "courseId", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonManagedReference
    private Course course;
    @JoinColumn(name = "studentId", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonIgnore
    private Student student;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "studentCourse")
    @JsonManagedReference
    private Collection<StudentDeclaration> studentCourseDeclarations;
    @ManyToMany
    @JoinTable(name = "studentCourseRequirement", joinColumns = {
        @JoinColumn(name = "studentCourseId", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "requirementId", referencedColumnName = "id")})
    @JsonManagedReference
    private Collection<Requirement> studentRequirements;
    @OneToMany(mappedBy = "studentCourse", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Collection<StudentCourseSitting> studentCourseSittings;
    @JoinColumn(name = "sittingId", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = true)
    @JsonManagedReference
    private Sitting firstSitting;
    @JoinColumn(name = "partId", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @JsonIgnore
    private Part currentPart;
    @JoinColumn(name = "sectionId", referencedColumnName = "id")
    @ManyToOne(optional = true)
    @JsonManagedReference
    @JsonIgnore
    private Section currentSection;
    @JoinColumn(name = "levelId", referencedColumnName = "id")
    @ManyToOne(optional = true)
    @JsonManagedReference
    private Level currentLevel;
    @OneToMany(mappedBy = "studentCourse", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Collection<StudentCourseQualification> qualifications;
    @Transient
    private ElligiblePart eligiblePart;
    @Transient
    private ElligibleLevel eligibleLevel;
    @Transient
    private Collection<Paper> exemptedPapers;
    @OneToMany(mappedBy = "studentCourse")
    @JsonIgnore
    private Collection<StudentCourseSubscription> subscriptions;
    @OneToOne(optional = false)
    @JoinColumn(name = "currentSubscriptionId", referencedColumnName = "id")
    @JsonIgnore
    private StudentCourseSubscription currentSubscription;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Transient
    private Date nextRenewal;

    public StudentCourse() {
    }

    public StudentCourse(Integer id) {
        this.id = id;
    }

    public StudentCourse(Integer id, Date created, boolean verified) {
        this.id = id;
        this.created = created;
        this.verified = verified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public boolean isActive() {
        return active;
    }

    public Date getNextRenewal() {
        try {
            nextRenewal = currentSubscription.getExpiry();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nextRenewal;
    }

    public void setNextRenewal(Date nextRenewal) {
        this.nextRenewal = nextRenewal;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(Date dateVerified) {
        this.dateVerified = dateVerified;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Collection<Requirement> getStudentRequirements() {
        return studentRequirements;
    }

    public void setStudentRequirements(Collection<Requirement> studentRequirements) {
        this.studentRequirements = studentRequirements;
    }

    public Collection<StudentCourseSitting> getStudentCourseSittings() {
        return studentCourseSittings;
    }

    public void setStudentCourseSittings(Collection<StudentCourseSitting> studentCourseSittings) {
        this.studentCourseSittings = studentCourseSittings;
    }

    public Sitting getFirstSitting() {
        return firstSitting;
    }

    public void setFirstSitting(Sitting firstSitting) {
        this.firstSitting = firstSitting;
    }

    public Collection<StudentDeclaration> getStudentCourseDeclarations() {
        return studentCourseDeclarations;
    }

    public void setStudentCourseDeclarations(Collection<StudentDeclaration> studentCourseDeclarations) {

        this.studentCourseDeclarations = studentCourseDeclarations;
    }

    public Part getCurrentPart() {
        return currentPart;
    }

    public void setCurrentPart(Part currentPart) {
        this.currentPart = currentPart;
    }

    public Section getCurrentSection() {
        return currentSection;
    }

    public void setCurrentSection(Section currentSection) {
        this.currentSection = currentSection;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public ElligiblePart getEligiblePart() {
        return eligiblePart;
    }

    public void setEligiblePart(ElligiblePart eligiblePart) {
        this.eligiblePart = eligiblePart;
    }

    public ElligibleLevel getEligibleLevel() {
        return eligibleLevel;
    }

    public void setEligibleLevel(ElligibleLevel eligibleLevel) {
        this.eligibleLevel = eligibleLevel;
    }

    public Collection<StudentCourseQualification> getQualifications() {
        return qualifications;
    }

    public void setQualifications(Collection<StudentCourseQualification> qualifications) {
        this.qualifications = qualifications;
    }

    public Collection<Paper> getExemptedPapers() {
        return exemptedPapers;
    }

    public void setExemptedPapers(Collection<Paper> exemptedPapers) {
        this.exemptedPapers = exemptedPapers;
    }

    public Collection<StudentCourseSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Collection<StudentCourseSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public StudentCourseSubscription getCurrentSubscription() {
        return currentSubscription;
    }

    public void setCurrentSubscription(StudentCourseSubscription currentSubscription) {
        this.currentSubscription = currentSubscription;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final StudentCourse other = (StudentCourse) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
