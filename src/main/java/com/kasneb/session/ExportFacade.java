/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import java.io.IOException;
import java.util.HashMap;
import javax.ejb.Stateless;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author jikara
 */
@Stateless
public class ExportFacade {

    public com.kasneb.jasper.ReceiptDocument generateReceipt(String receiptNumber) throws IOException, JRException {
        return new com.kasneb.jasper.ReceiptDocument(new HashMap<>(), null);
    }
}
