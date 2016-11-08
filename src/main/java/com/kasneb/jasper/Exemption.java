/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.jasper;

import java.util.List;

/**
 *
 * @author jikara
 */
public class Exemption {

    private String courseName;
    private List<ExemptionPart> parts;
    private List<ExemptionLevel> levels;

    public Exemption() {
    }

    public Exemption(String courseName, List<ExemptionPart> parts, List<ExemptionLevel> levels) {
        this.courseName = courseName;
        this.parts = parts;
        this.levels = levels;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<ExemptionPart> getParts() {
        return parts;
    }

    public void setParts(List<ExemptionPart> parts) {
        this.parts = parts;
    }

    public List<ExemptionLevel> getLevels() {
        return levels;
    }

    public void setLevels(List<ExemptionLevel> levels) {
        this.levels = levels;
    }

    public class ExemptionPart {

        private String name;
        private List<ExemptionSection> sections;

        public ExemptionPart() {
        }

        public ExemptionPart(String name, List<ExemptionSection> sections) {
            this.name = name;
            this.sections = sections;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ExemptionSection> getSections() {
            return sections;
        }

        public void setSections(List<ExemptionSection> sections) {
            this.sections = sections;
        }

        public class ExemptionSection {

            private String name;
            private List<ExemptionPaper> papers;

            public ExemptionSection() {
            }

            public ExemptionSection(String name, List<ExemptionPaper> papers) {
                this.name = name;
                this.papers = papers;
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
        }

    }

    public class ExemptionLevel {

        private List<ExemptionPaper> papers;

        public ExemptionLevel() {
        }

        public ExemptionLevel(List<ExemptionPaper> papers) {
            this.papers = papers;
        }

        public List<ExemptionPaper> getPapers() {
            return papers;
        }

        public void setPapers(List<ExemptionPaper> papers) {
            this.papers = papers;
        }
    }

    public class ExemptionPaper {

        private String code;
        private String name;

        public ExemptionPaper() {
        }

        public ExemptionPaper(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
