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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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
    private String registrationNumber;
    @Basic(optional = false)
    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date created = new Date();
    @Column(name = "document")
    private String document;
    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    private Boolean active;
    @Basic(optional = false)
    @Column(name = "verified", nullable = false)
    private Boolean verified = false;
    @Column(name = "dateVerified")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date dateVerified;
    @ManyToOne(optional = true)
    @JoinColumn(name = "verifiedBy")
    @JsonManagedReference
    private User verifiedBy;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "verificationStatus")
    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;
    @JoinColumn(name = "courseId", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    @JsonManagedReference
    private KasnebCourse course;
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
    @Transient
    private ElligiblePart eligiblePart;
    @Transient
    private ElligibleLevel eligibleLevel;
    @OneToMany(mappedBy = "studentCourse", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<StudentCourseSubscription> subscriptions;
    @Transient
    @JsonManagedReference
    private StudentCourseSubscription currentSubscription;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Transient
    private Date nextRenewal;
    @OneToMany(mappedBy = "studentCourse", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<KasnebStudentCourseQualification> kasnebQualifications;
    @OneToMany(mappedBy = "studentCourse", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<OtherStudentCourseQualification> otherQualifications;
    @Transient
    @JsonIgnore
    private Set<StudentCourseQualification> qualifications;
    @OneToMany(mappedBy = "studentCourse")
    @JsonManagedReference
    private Set<StudentCourseExemptionPaper> exemptions;
    @Transient
    private Set<Paper> eligibleExemptions;
    @Transient
    private Set<Paper> exemptedPapers = new HashSet<>();
    @Transient
    private KasnebStudentCourseQualification kasnebQualification;
    @Transient
    private OtherStudentCourseQualification otherQualification;
    @OneToMany(mappedBy = "studentCourse", fetch = FetchType.EAGER)
    @JsonManagedReference
    private Collection<Invoice> invoices;
    @Transient
    private Student studentObj;

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

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Date getNextRenewal() {
        if (getCurrentSubscription() != null) {
            nextRenewal = getCurrentSubscription().getExpiry();
        }
        return nextRenewal;
    }

    public void setNextRenewal(Date nextRenewal) {
        this.nextRenewal = nextRenewal;
    }

    public Date getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(Date dateVerified) {
        this.dateVerified = dateVerified;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public KasnebCourse getCourse() {
        return course;
    }

    public void setCourse(KasnebCourse course) {
        this.course = course;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Student getStudentObj() {
        if (getStudent() != null) {
            studentObj = new Student(getStudent().getFirstName(), getStudent().getMiddleName(), getStudent().getLastName(), getStudent().getPhoneNumber(), getStudent().getGender(), getStudent().getEmail());
            studentObj.setId(getStudent().getId());
        }
        return studentObj;
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
        Collection<ElligibleSection> elligibleSections = getElligibleSections();
        if (getCurrentPart() != null) {
            return new ElligiblePart(getCurrentPart().getName(), elligibleSections);
        }
        return null;
    }

    public Collection<ElligibleSection> getElligibleSections() {
        Collection<ElligibleSection> elligibleSections = new ArrayList<>();
        if (getCurrentPart() != null) {
            for (Section section : getCurrentPart().getSectionCollection()) {
                Collection<Paper> elligiblePapers = getEligiblePapers(section.getPaperCollection(), getExemptedPapers(), getPassedPapers());
                ElligibleSection mySection = new ElligibleSection(section.getName(), elligiblePapers, section.isOptional());
                if (!mySection.getPapers().isEmpty()) {
                    elligibleSections.add(mySection);
                }
            }
        }
        return elligibleSections;
    }

    private Collection<Paper> getEligiblePapers(Collection<Paper> papers, Collection<Paper> exempted, Collection<Paper> passedPapers) {
        papers.removeAll(exempted); //remove exempted
        papers.removeAll(passedPapers); //remove passed
        return papers;
    }

    private Collection<Paper> getPassedPapers() {
        Collection<Paper> passedPapers = new ArrayList<>();
        for (StudentCourseSitting sitting : getStudentCourseSittings()) {
            for (StudentCourseSittingPaper sp : sitting.getPapers()) {
                passedPapers.add(sp.getPaper());
            }
        }
        return passedPapers;
    }

    public ElligibleLevel getEligibleLevel() {
        return eligibleLevel;
    }

    public void setEligibleLevel(ElligibleLevel eligibleLevel) {
        this.eligibleLevel = eligibleLevel;
    }

    public Collection<StudentCourseSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Collection<StudentCourseSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public StudentCourseSubscription getCurrentSubscription() {
        if (getSubscriptions() != null) {
            for (StudentCourseSubscription subscription : getSubscriptions()) {
                if (subscription.isCurrent()) {
                    currentSubscription = subscription;
                }
            }
        }
        return currentSubscription;
    }

    public void setCurrentSubscription(StudentCourseSubscription currentSubscription) {
        this.currentSubscription = currentSubscription;
    }

    public Set<KasnebStudentCourseQualification> getKasnebQualifications() {
        return kasnebQualifications;
    }

    public void setKasnebQualifications(Set<KasnebStudentCourseQualification> kasnebQualifications) {
        this.kasnebQualifications = kasnebQualifications;
    }

    public Set<OtherStudentCourseQualification> getOtherQualifications() {
        return otherQualifications;
    }

    public void setOtherQualifications(Set<OtherStudentCourseQualification> otherQualifications) {
        this.otherQualifications = otherQualifications;
    }

    public Set<StudentCourseExemptionPaper> getExemptions() {
        return exemptions;
    }

    public void setExemptions(Set<StudentCourseExemptionPaper> exemptions) {
        this.exemptions = exemptions;
    }

    public Set<StudentCourseQualification> getQualifications() {
        qualifications = new HashSet<>();
        try {
            for (OtherStudentCourseQualification oq : otherQualifications) {
                qualifications.add(oq);
            }
        } catch (java.lang.NullPointerException npe) {

        }
        try {
            for (StudentCourseQualification kq : kasnebQualifications) {
                qualifications.add(kq);
            }
        } catch (java.lang.NullPointerException npe) {

        }
        return qualifications;
    }

//    public Set<Paper> getEligibleExemptions() {
//        eligibleExemptions = new HashSet<>();
//        try {
//            for (StudentCourseQualification qualification : getQualifications()) {
//                for (CourseExemption courseExemption : qualification.getQualification().getCourseExemptions()) {
//                    eligibleExemptions.add(courseExemption.getPaper());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return eligibleExemptions;
//    }
//
//    public void setEligibleExemptions(Set<Paper> eligibleExemptions) {
//        this.eligibleExemptions = eligibleExemptions;
//    }
    public Set<Paper> getEligibleExemptions() {
        eligibleExemptions = new HashSet<>();
        if (getKasnebQualifications() != null) {
            for (StudentCourseQualification qualification : getKasnebQualifications()) {
                for (CourseExemption courseExemption : qualification.getQualification().getCourseExemptions()) {
                    eligibleExemptions.add(courseExemption.getPaper());
                }
            }
        }
        return eligibleExemptions;
    }

    public void setEligibleExemptions(Set<Paper> eligibleExemptions) {
        this.eligibleExemptions = eligibleExemptions;
    }

    public Set<Paper> getExemptedPapers() {
        if (getExemptions() != null) {
            for (StudentCourseExemptionPaper e : getExemptions()) {
                if (e != null && e.getPaid() && e.getVerified()) {
                    exemptedPapers.add(e.getPaper());
                }
            }
        }
        return exemptedPapers;
    }

    public void setExemptedPapers(Set<Paper> exemptedPapers) {
        this.exemptedPapers = exemptedPapers;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public KasnebStudentCourseQualification getKasnebQualification() {
        return kasnebQualification;
    }

    public void setKasnebQualification(KasnebStudentCourseQualification kasnebQualification) {
        this.kasnebQualification = kasnebQualification;
    }

    public OtherStudentCourseQualification getOtherQualification() {
        return otherQualification;
    }

    public void setOtherQualification(OtherStudentCourseQualification otherQualification) {
        this.otherQualification = otherQualification;
    }

    public Collection<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Collection<Invoice> invoices) {
        this.invoices = invoices;
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
        if (!Objects.equals(this.course.getId(), other.course.getId())) {
            return false;
        }
        return Objects.equals(this.student.getId(), other.student.getId());
    }

    public void setEligiblePart(ElligiblePart elligiblePart) {
        this.eligiblePart = elligiblePart;
    }

}
