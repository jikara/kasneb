/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kasneb.client.Stream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import javax.persistence.JoinColumns;
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
    @NamedQuery(name = "StudentCourse.findAll", query = "SELECT s FROM StudentCourse s")
    ,@NamedQuery(name = "StudentCourse.findByRegistrationNumber", query = "SELECT s FROM StudentCourse s WHERE s.registrationNumber = :registrationNumber")
    ,@NamedQuery(name = "StudentCourse.findByCreated", query = "SELECT s FROM StudentCourse s WHERE s.created = :created")
    ,@NamedQuery(name = "StudentCourse.findByVerified", query = "SELECT s FROM StudentCourse s WHERE s.verified = :verified")
    ,@NamedQuery(name = "StudentCourse.findByDateVerified", query = "SELECT s FROM StudentCourse s WHERE s.dateVerified = :dateVerified")
    ,@NamedQuery(name = "StudentCourse.findByRemarks", query = "SELECT s FROM StudentCourse s WHERE s.remarks = :remarks")})
public class StudentCourse implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "registrationNumber")
    private Integer registrationNumber;
    @JsonInclude
    private transient String fullRegistrationNumber;
    @Basic(optional = false)
    @Column(name = "created", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Nairobi")
    private Date created = new Date();
    @OneToMany(mappedBy = "studentCourse", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentCourseDocument> documents;
    @Basic(optional = false)
    @Column(name = "active", nullable = false)
    private Boolean active;
    @Basic(optional = false)
    @Column(name = "verified", nullable = false)
    private Boolean verified = false;
    @Column(name = "dateVerified")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Africa/Nairobi")
    private Date dateVerified;
    @ManyToOne(optional = true)
    @JoinColumn(name = "verifiedBy")
    private User verifiedBy;
    @Column(name = "remarks")
    private String remarks;
    @Column(name = "verificationStatus")
    @Enumerated(EnumType.STRING)
    private VerificationStatus verificationStatus;
    @Column(name = "courseStatus")
    @Enumerated(EnumType.STRING)
    private StudentCourseStatus courseStatus = StudentCourseStatus.PENDING;
    @JoinColumn(name = "courseId", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private KasnebCourse course;
    @JoinColumn(name = "studentId", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonBackReference(value = "student-current-course")
    private Student student;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "studentCourse", fetch = FetchType.LAZY)
    private Collection<StudentDeclaration> studentCourseDeclarations;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "studentCourseRequirement", joinColumns = {
        @JoinColumn(name = "studentCourseId", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "requirementId", referencedColumnName = "id")})
    private Collection<Requirement> studentRequirements;
    @OneToMany(mappedBy = "studentCourse", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Collection<StudentCourseSitting> studentCourseSittings;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "studentCoursePaper", joinColumns = {
        @JoinColumn(name = "studentCourseId", referencedColumnName = "id")
    }, inverseJoinColumns = {
        @JoinColumn(name = "paperCode", referencedColumnName = "code")
    })
    private Collection<Paper> elligiblePapers;
    @JoinColumn(name = "sittingId", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Sitting firstSitting;
    @ManyToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "partId", referencedColumnName = "id", insertable = false, updatable = false)
        ,@JoinColumn(name = "courseId", referencedColumnName = "courseId", insertable = false, updatable = false)})
    private Part currentPart;
    @ManyToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    @JoinColumns({
        @JoinColumn(name = "levelId", referencedColumnName = "id", insertable = false, updatable = false)
        ,@JoinColumn(name = "courseId", referencedColumnName = "courseId", insertable = false, updatable = false)})
    private Level currentLevel;
    @Column(name = "partId")
    private Integer currentPartId;
    @Column(name = "levelId")
    private Integer currentLevelId;
    @JsonInclude
    private transient ElligiblePart eligiblePart;
    @JsonInclude
    private transient Set<ElligibleLevel> eligibleLevels;
    @OneToMany(mappedBy = "studentCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<StudentCourseSubscription> subscriptions;
    @JsonIgnore
    private transient StudentCourseSubscription lastSubscription;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", timezone = "Africa/Nairobi")
    @Transient
    private Date nextRenewal;
    @OneToMany(mappedBy = "studentCourse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<Exemption> exemptions;
    @JsonInclude
    private transient List<Paper> eligibleExemptions;
    @Transient
    private Set<Paper> exemptedPapers;
    @Transient
    private OtherStudentQualification otherQualification;
    @Transient
    private KasnebStudentQualification kasnebQualification;
    @OneToMany(mappedBy = "studentCourse", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Collection<Invoice> invoices;
    @OneToMany(mappedBy = "studentCourse", cascade = CascadeType.ALL)
    private Collection<Payment> payments;
    @JsonInclude
    private transient Student studentObj;
    @JsonInclude
    private transient String renewalStatus;
    @Transient
    private Integer qualificationId;
    @Transient
    private StudentDeclaration learnAbout;
    @Column(name = "rejectCode")
    private String rejectCode;

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

    public StudentCourse(Integer registrationNumber, Boolean active, Date dateVerified, User verifiedBy, String remarks, VerificationStatus verificationStatus, KasnebCourse course, Sitting firstSitting, Boolean verified) {
        this.registrationNumber = registrationNumber;
        this.active = active;
        this.dateVerified = dateVerified;
        this.verifiedBy = verifiedBy;
        this.remarks = remarks;
        this.verificationStatus = verificationStatus;
        this.course = course;
        this.firstSitting = firstSitting;
        this.verified = verified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(Integer registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getFullRegistrationNumber() {
        if (getCourse() != null && getRegistrationNumber() != null) {
            if (getCourse().getId() != null) {
                switch (getCourse().getId()) {
                    case "01":
                        fullRegistrationNumber = "NAC/" + getRegistrationNumber();
                        break;
                    case "02":
                        fullRegistrationNumber = "NSC/" + getRegistrationNumber();
                        break;
                    case "06":
                        fullRegistrationNumber = "CTP/" + getRegistrationNumber();
                        break;
                    case "08":
                        fullRegistrationNumber = "CMP/" + getRegistrationNumber();
                        break;
                    case "10":
                        fullRegistrationNumber = "ISP/" + getRegistrationNumber();
                        break;
                    case "11":
                        fullRegistrationNumber = "FAQ/" + getRegistrationNumber();
                        break;
                    case "12":
                        fullRegistrationNumber = "ATD/" + getRegistrationNumber();
                        break;
                    case "13":
                        fullRegistrationNumber = "DIC/" + getRegistrationNumber();
                        break;
                    case "14":
                        fullRegistrationNumber = "DCM/" + getRegistrationNumber();
                        break;
                }
            }
        }
        return fullRegistrationNumber;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<StudentCourseDocument> getDocuments() {
        return documents;
    }

    public void setDocuments(List<StudentCourseDocument> documents) {
        if (documents != null) {
            for (StudentCourseDocument doc : documents) {
                doc.setStudentCourse(this);
            }
        }
        this.documents = documents;
    }

    public User getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

    public Date getNextRenewal() {
        if (this.getLastSubscription() != null) {
            nextRenewal = getLastSubscription().getExpiry();
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

    public StudentCourseStatus getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(StudentCourseStatus courseStatus) {
        this.courseStatus = courseStatus;
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
            Student myStudent = getStudent();
            studentObj = new Student(getStudent().getFirstName(), getStudent().getMiddleName(), getStudent().getLastName(), getStudent().getPhoneNumber(), getStudent().getEmail());
            studentObj.setId(myStudent.getId());
            studentObj.setNationality(getStudent().getNationality());
            studentObj.setDateOfBirth(getStudent().getDateOfBirth());
            studentObj.setGender(getStudent().getGender());
            studentObj.setDocumentNo(getStudent().getDocumentNo());
            studentObj.setContact(getStudent().getContact());
            StudentCourse myCurrentCourse = new StudentCourse(getId());
            myCurrentCourse.setCourse(getCourse());
            myCurrentCourse.setExemptions(getExemptions());
            //studentObj.setCurrentCourse(myCurrentCourse);
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
        if (studentCourseSittings != null) {
            for (StudentCourseSitting sitting : studentCourseSittings) {
                sitting.setStudentCourse(this);
            }
        }
        this.studentCourseSittings = studentCourseSittings;
    }

    public Collection<Paper> getElligiblePapers() {
        return elligiblePapers;
    }

    public void setElligiblePapers(Collection<Paper> elligiblePapers) {
        this.elligiblePapers = elligiblePapers;
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

    public Collection<StudentCourseSubscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Collection<StudentCourseSubscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public StudentCourseSubscription getLastSubscription() {
        Integer maximumYear = 0;
        if (getSubscriptions() != null) {
            for (StudentCourseSubscription subscription : getSubscriptions()) {
                Integer subscrYear = subscription.getYear();
                if (subscrYear > maximumYear) {
                    maximumYear = subscrYear;
                }
            }
            for (StudentCourseSubscription subscription : getSubscriptions()) {
                if (subscription.getYear().equals(maximumYear)) {
                    lastSubscription = subscription;
                }
            }
        }
        return lastSubscription;
    }

    public void setLastSubscription(StudentCourseSubscription lastSubscription) {
        this.lastSubscription = lastSubscription;
    }

    public KasnebStudentQualification getKasnebQualification() {
        return kasnebQualification;
    }

    public void setKasnebQualification(KasnebStudentQualification kasnebQualification) {
        this.kasnebQualification = kasnebQualification;
    }

    public Collection<Exemption> getExemptions() {
        return exemptions;
    }

    public void setExemptions(Collection<Exemption> exemptions) {
        if (exemptions != null) {
            for (Exemption e : exemptions) {
                e.setStudentCourse(this);
            }
        }
        this.exemptions = exemptions;
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

    public OtherStudentQualification getOtherQualification() {
        return otherQualification;
    }

    public void setOtherQualification(OtherStudentQualification otherQualification) {
        this.otherQualification = otherQualification;
    }

    public Collection<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Collection<Invoice> invoices) {
        if (invoices != null) {
            for (Invoice i : invoices) {
                i.setStudentCourse(this);
            }
        }
        this.invoices = invoices;
    }

    public Collection<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Collection<Payment> payments) {
        for (Payment p : payments) {
            p.setStudentCourse(this);
        }
        this.payments = payments;
    }

    public boolean isUptoDate() {
        if (getNextRenewal() != null) {
            if (new Date().after(getNextRenewal()) && this.getCourseStatus() != StudentCourseStatus.COMPLETED) {
                return false;
            }
        }
        return true;
    }

    public String getRenewalStatus() {
        renewalStatus = "Not upto date";
        if (isUptoDate()) {
            renewalStatus = "Upto date";
        }
        return renewalStatus;
    }

    public Stream getStream() {
        if (getCourse() != null) {
            switch (getCourse().getId()) {
                case "01":
                    return Stream.AC;
                case "02":
                    return Stream.SC;
                case "03":
                    return Stream.AC;
                case "04":
                    return Stream.AC;
            }
        }
        return Stream.AC;
    }

    public Integer getQualificationId() {
        return qualificationId;
    }

    public void setQualificationId(Integer qualificationId) {
        this.qualificationId = qualificationId;
    }

    public Part getCurrentPart() {
        return currentPart;
    }

    public void setCurrentPart(Part currentPart) {
        this.currentPart = currentPart;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Integer getCurrentPartId() {
        return currentPartId;
    }

    public void setCurrentPartId(Integer currentPartId) {
        this.currentPartId = currentPartId;
    }

    public Integer getCurrentLevelId() {
        return currentLevelId;
    }

    public void setCurrentLevelId(Integer currentLevelId) {
        this.currentLevelId = currentLevelId;
    }

    //Transient fields  //test
    public StudentDeclaration getLearnAbout() {
        if (this.getStudentCourseDeclarations() != null) {
            for (StudentDeclaration studentDeclaration : getStudentCourseDeclarations()) {
                if (studentDeclaration.getStudentDeclarationPK().getDeclarationId() == 10) {
                    learnAbout = studentDeclaration;
                }
            }
        }
        return learnAbout;
    }

    public ElligiblePart getEligiblePart() {
        boolean optional = false;
        Set<ElligibleSection> elligibleSections = new HashSet<>();
        if (getElligiblePapers() != null && this.getCurrentPart() != null) {
            for (Paper p : getElligiblePapers()) {
                if (p.getSection().getPart().equals(getCurrentPart())) {
                    ElligibleSection elligibleSection = new ElligibleSection(p.getSection().getName(), p.getSection(), new ArrayList<>(), p.getSection().isOptional());
                    elligibleSections.add(elligibleSection);
                }
            }
            for (ElligibleSection elligibleSection : elligibleSections) {
                for (Paper p : getElligiblePapers()) {
                    if (p.getSection().getSectionPK().equals(elligibleSection.getSection().getSectionPK())) {
                        elligibleSection.addPaper(p);
                    }
                }
            }
            for (ElligibleSection elligibleSection : elligibleSections) {
                if (elligibleSection.getPapers().size() < 1) {
                    elligibleSections.remove(elligibleSection);
                }
                if (elligibleSections.size() < 2) {
                    elligibleSection.setOptional(false);
                }
            }
            return new ElligiblePart(getCurrentPart().getName(), new ArrayList(elligibleSections));
        }
        return null;
    }

    public Set<ElligibleLevel> getEligibleLevels() {
        eligibleLevels = new HashSet<>();
        if (getElligiblePapers() != null && getCurrentLevel() != null) {
            ElligibleLevel eliggibleLevel = new ElligibleLevel(getCurrentLevel().getName(), getCurrentLevel(), new ArrayList<>());
            for (Paper p : getElligiblePapers()) {
                if (p.getLevel().equals(getCurrentLevel())) {
                    eliggibleLevel.addPaper(p);
                }
            }
            eligibleLevels.add(eliggibleLevel);
        }
        return eligibleLevels;
    }

    public ElligibleLevel getEligibleLevel() {
        boolean optional = false;
//        Collection<Paper> elligiblePapers;
//        if (getCurrentLevel() != null) {
//            elligiblePapers = getCurrentLevel().getPaperCollection();
//            elligiblePapers.removeAll(getExemptedPapers()); //Remove exempted papers                
//            return new ElligibleLevel(getCurrentLevel().getName(), elligiblePapers);
//        }
        return null;
    }

    public List<Paper> getEligibleExemptions() {
        eligibleExemptions = new ArrayList<>();
        Set<Paper> papers = new HashSet<>();
        if (getStudent() != null) {
            if (getStudent().getKasnebQualifications() != null) {
                for (KasnebStudentQualification qualification : getStudent().getKasnebQualifications()) {
                    if (qualification.getQualification() != null) {
                        for (CourseExemption courseExemption : qualification.getQualification().getCourseExemptions()) {
                            if (courseExemption.getCourse().equals(this.getCourse())) {
                                papers.add(courseExemption.getPaper());
                            }
                        }
                    }
                }
            }
        }
        papers.removeAll(getExemptedPapers()); //Remove exempted papers
        eligibleExemptions.addAll(papers);
        Collections.sort(eligibleExemptions, new Comparator<Paper>() {
            @Override
            public int compare(Paper p1, Paper p2) {
                return p1.getCode().compareTo(p2.getCode());
            }
        });
        return eligibleExemptions;
    }

    public Set<Paper> getExemptedPapers() {
        exemptedPapers = new HashSet<>();
        if (getExemptions() != null) {
            for (Exemption exemption : getExemptions()) {
                if (exemption.getPapers() != null) {
                    for (ExemptionPaper exemptionPaper : exemption.getPapers()) {
                        if (exemptionPaper != null) {
                            if (exemptionPaper.getVerificationStatus() != null && exemptionPaper.isElligible()) {
                                exemptedPapers.add(exemptionPaper.getPaper());
                            }
                        }
                    }
                }
            }
        }
        return exemptedPapers;
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(String rejectCode) {
        this.rejectCode = rejectCode;
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

}
