/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.jasper;

import com.kasneb.entity.Paper;
import com.kasneb.entity.Part;
import com.kasneb.entity.Section;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author jikara
 */
public class ExemptionDocument {

    private String reference;
    private String date;
    private String studentAndReg;
    private String studentName;
    private String regNo;
    private String address;
    private String city;
    private String country;
    private String issuedBy;
    private String letterTitle;
    private String courseName;
    private Set<ExemptionPart> parts;
    private Set<ExemptionLevel> levels;

    public ExemptionDocument() {
    }

    public ExemptionDocument(String courseName, String reference, String date, String regNo, String studentName, String address, String city, String country, String issuedBy) {
        this.courseName = courseName;
        this.reference = reference;
        this.date = date;
        this.regNo = regNo;
        this.studentName = studentName;
        this.address = address;
        this.city = city;
        this.country = country;
        this.issuedBy = issuedBy;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudentAndReg() {
        studentAndReg = getStudentName() + " - " + getRegNo();
        return studentAndReg;
    }

    public void setStudentAndReg(String studentAndReg) {
        this.studentAndReg = studentAndReg;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getLetterTitle() {
        letterTitle = "EXEMPTION IN THE " + courseName + " EXAMINATION - " + getRegNo();
        return letterTitle;
    }

    public void setLetterTitle(String letterTitle) {
        this.letterTitle = letterTitle;
    }

    public Set<ExemptionPart> getParts() {
        return parts;
    }

    public void setParts(Set<ExemptionPart> parts) {
        this.parts = parts;
    }

    public Set<ExemptionLevel> getLevels() {
        return levels;
    }

    public void setLevels(Set<ExemptionLevel> levels) {
        this.levels = levels;
    }

    public void addParts(ExemptionPart part) {
        Set<ExemptionPart> myParts = new HashSet<>();
        myParts.add(part);
        this.parts = myParts;
    }

    public void addLevels(ExemptionLevel level) {
        Set<ExemptionLevel> myLevels = new HashSet<>();
        myLevels.add(level);
        this.levels = myLevels;
    }

    public class ExemptionPart {

        private Integer id;
        private Part part;
        private String name;
        private List<ExemptionSection> sections;

        public ExemptionPart() {
        }

        public ExemptionPart(Integer id, Part part, String name, List<ExemptionSection> sections) {
            this.id = id;
            this.part = part;
            this.name = name;
            this.sections = sections;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Part getPart() {
            return part;
        }

        public void setPart(Part part) {
            this.part = part;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ExemptionSection> getSections() {
            Collections.sort(sections, new Comparator<ExemptionSection>() {
                @Override
                public int compare(ExemptionSection s1, ExemptionSection s2) {
                    return s1.getId().compareTo(s2.getId());
                }
            });
            return sections;
        }

        public void setSections(List<ExemptionSection> sections) {
            this.sections = sections;
        }

        public void addExemptionSection(ExemptionSection section) {
            if (section.getSection().getPart().equals(this.getPart())) {
                sections.add(section);
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + Objects.hashCode(this.id);
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
            final ExemptionPart other = (ExemptionPart) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            return true;
        }

        public class ExemptionSection {

            private Integer id;
            private Part part;
            private Section section;
            private String name;
            private List<ExemptionPaper> papers;

            public ExemptionSection() {
            }

            public ExemptionSection(Part part, Section section, List<ExemptionPaper> papers) {
                this.part = part;
                this.section = section;
                this.papers = papers;
            }

            public Section getSection() {
                return section;
            }

            public void setSection(Section section) {
                this.section = section;
            }

            public Part getPart() {
                return part;
            }

            public void setPart(Part part) {
                this.part = part;
            }

            public List<ExemptionPaper> getPapers() {
                Collections.sort(papers, new Comparator<ExemptionPaper>() {
                    @Override
                    public int compare(ExemptionPaper p1, ExemptionPaper p2) {
                        return p1.getPaperCode().compareTo(p2.getPaperCode());
                    }
                });
                return papers;
            }

            public void setPapers(List<ExemptionPaper> papers) {
                this.papers = papers;
            }

            public Integer getId() {
                if (this.getSection() != null) {
                    id = getSection().getSectionPK().getId();
                }
                return id;
            }

            public String getName() {
                if (this.getSection() != null) {
                    name = getSection().getName();
                }
                return name;
            }

            public void addPaper(ExemptionPaper paper) {
                if (paper.getPaper() != null) {
                    if (paper.getPaper().getSection().equals(this.getSection())) {
                        papers.add(paper);
                    }
                }
            }

            @Override
            public int hashCode() {
                int hash = 3;
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
                final ExemptionSection other = (ExemptionSection) obj;
                if (!Objects.equals(this.id, other.id)) {
                    return false;
                }
                return true;
            }
        }

    }

    public class ExemptionLevel {

        private Integer id;
        private String name;
        private List<ExemptionPaper> papers;

        public ExemptionLevel() {
        }

        public ExemptionLevel(Integer id, String name, List<ExemptionPaper> papers) {
            this.id = id;
            this.name = name;
            this.papers = papers;
        }

        public ExemptionLevel(List<ExemptionPaper> papers) {
            this.papers = papers;
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

        public List<ExemptionPaper> getPapers() {
            return papers;
        }

        public void setPapers(List<ExemptionPaper> papers) {
            this.papers = papers;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.id);
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
            final ExemptionLevel other = (ExemptionLevel) obj;
            if (!Objects.equals(this.id, other.id)) {
                return false;
            }
            return true;
        }
    }

    public class ExemptionPaper {

        private Paper paper;
        private String paperCode;
        private String paperName;

        public ExemptionPaper(Paper paper) {
            this.paper = paper;
        }

        public Paper getPaper() {
            return paper;
        }

        public void setPaper(Paper paper) {
            this.paper = paper;
        }

        public String getPaperCode() {
            if (this.getPaper() != null) {
                paperCode = getPaper().getCode();
            }
            return paperCode;
        }

        public String getPaperName() {
            if (this.getPaper() != null) {
                paperName = getPaper().getName();
            }
            return paperName;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 79 * hash + Objects.hashCode(this.paper);
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
            final ExemptionPaper other = (ExemptionPaper) obj;
            if (!Objects.equals(this.paper, other.paper)) {
                return false;
            }
            return true;
        }
    }
}
