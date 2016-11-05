/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author jikara
 */
public class ExportUtil {

    public InputStream generateReceipt(com.kasneb.jasper.ReceiptDocument receipt) throws IOException, JRException {
        JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(new ArrayList<>());
        InputStream inStream = loadJasper("Receipt");
        JasperPrint jasperPrint = JasperFillManager.fillReport(inStream, new HashMap(), beanCollectionDataSource);
        byte[] byteArray = JasperExportManager.exportReportToPdf(jasperPrint);
        ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
        return bis;
    }

    public InputStream loadJasper(String jasperFileName) throws FileNotFoundException {
        File file = new File("C:\\Users\\jikara\\Documents\\NetBeansProjects\\kasneb_new\\src\\main\\webapp\\WEB-INF\\jasper\\Timetable.jasper");
        //InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("Web-INF/jasper/Receipt.jasper");
        return new FileInputStream(file);
//return new FileInputStream(initialFile);
    }
}
