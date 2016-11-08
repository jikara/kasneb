/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.jasper.Exemption;
import com.kasneb.jasper.Exemption.ExemptionPaper;
import com.kasneb.jasper.Exemption.ExemptionPart;
import com.kasneb.jasper.Exemption.ExemptionPart.ExemptionSection;
import com.kasneb.jasper.ExemptionDocument;
import com.kasneb.jasper.InvoiceDocument;
import com.kasneb.jasper.Paper;
import com.kasneb.jasper.ReceiptItem;
import com.kasneb.jasper.TimetableDocument;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author jikara
 */
@Stateless
public class ExportFacade {

    public com.kasneb.jasper.ReceiptDocument generateReceipt(String receiptNumber) {
        List<ReceiptItem> items = new ArrayList<>();
        ReceiptItem item = new ReceiptItem("Registration", new BigDecimal(3000));
        ReceiptItem item1 = new ReceiptItem("Administrative fee", new BigDecimal(2500));
        items.add(item);
        items.add(item1);
        return new com.kasneb.jasper.ReceiptDocument("KAS/423423/009", "JUSTUS IKARA", "P.O. BOX 60883 - 00200", "NAIROBI", "KENYA", "C1115677", "15/08/2016", "NAC/282954", "CPA", items);
    }

    public TimetableDocument generateTimetable(Integer id) {
        List<Paper> papers = new ArrayList<>();
        Paper p = new Paper("CD11", "Fundamentals of Credit Management", "Monday 23/05/2016", "9.00 A.M. - 12.00 NOON");
        papers.add(p);
        return new TimetableDocument("NAC/282954", "JUSTUS IKARA 26380045", "15/08/2016", "MAY 2016", "STAREHE BOYS CENTRE", "CPA Part 1 Section 1", papers);
    }

    public ExemptionDocument generateExemptionLetter(int i) {
        List<Exemption> exemptions = new ArrayList<>();
        List<ExemptionPart> parts = new ArrayList<>();
        List<ExemptionSection> sections = new ArrayList<>();
        List<ExemptionSection> sections2 = new ArrayList<>();
        List<ExemptionPaper> papersSec1 = new ArrayList<>();
        List<ExemptionPaper> papersSec2 = new ArrayList<>();
        List<ExemptionPaper> papersSec3 = new ArrayList<>();

        Exemption exemption = new Exemption();
        ExemptionPart part1 = exemption.new ExemptionPart("Part I", null);
        ExemptionPart part2 = exemption.new ExemptionPart("Part II", null);
        ExemptionSection section1part1 = part1.new ExemptionSection("Section 1", null);
        ExemptionSection section2part1 = part1.new ExemptionSection("Section 2", null);
        ExemptionSection section3part2 = part2.new ExemptionSection("Section 3", null);

        ExemptionPaper paper1 = exemption.new ExemptionPaper("CA11", "Financial Accounting");
        ExemptionPaper paper2 = exemption.new ExemptionPaper("CA12", "Introduction to law");
        ExemptionPaper paper3 = exemption.new ExemptionPaper("CA13", "Enterepereneurship and accounting");

        ExemptionPaper paper4 = exemption.new ExemptionPaper("CA21", "Economics");
        ExemptionPaper paper5 = exemption.new ExemptionPaper("CA22", "Auditing and Assurance");
        ExemptionPaper paper6 = exemption.new ExemptionPaper("CA23", "Financial Management");

        ExemptionPaper paper7 = exemption.new ExemptionPaper("CA31", "Agronomics");
        ExemptionPaper paper8 = exemption.new ExemptionPaper("CA32", "Test Subject");

        papersSec1.add(paper1);
        papersSec1.add(paper2);
        papersSec1.add(paper3);

        papersSec2.add(paper4);
        papersSec2.add(paper5);
        papersSec2.add(paper6);

        papersSec3.add(paper7);
        papersSec3.add(paper8);

        section1part1.setPapers(papersSec1);
        section2part1.setPapers(papersSec2);
        section3part2.setPapers(papersSec3);

        sections.add(section1part1);
        sections.add(section2part1);
        sections2.add(section3part2);
        part1.setSections(sections);
        part2.setSections(sections2);
        parts.add(part1);
        parts.add(part2);
        exemptions.add(new com.kasneb.jasper.Exemption("CPA", parts, null));
        return new ExemptionDocument("152452", "12/08/2016", "NAC/282954", "Justus Ikara", "54395 - 00200", "Nairobi", "Kenya", "Test", exemptions);
    }

    public InvoiceDocument generateInvoice(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
