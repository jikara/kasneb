/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.kasneb.entity.Invoice;
import com.kasneb.entity.InvoiceDetail;
import com.kasneb.entity.RenewalInvoiceDetail;
import com.kasneb.entity.StudentCourseSubscription;
import com.kasneb.entity.pk.StudentCourseSubscriptionPK;
import com.kasneb.util.DateUtil;
import java.text.ParseException;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jikara
 */
@Stateless
public class StudentCourseSubscriptionFacade extends AbstractFacade<StudentCourseSubscription> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentCourseSubscriptionFacade() {
        super(StudentCourseSubscription.class);
    }

    public void createSubscriptions(Invoice invoice) throws ParseException {
        for (InvoiceDetail invDetail : invoice.getInvoiceDetails()) {
            if (invDetail instanceof RenewalInvoiceDetail) {
                RenewalInvoiceDetail renewalInvDetail = (RenewalInvoiceDetail) invDetail;
                StudentCourseSubscriptionPK pk = new StudentCourseSubscriptionPK(invoice.getStudentCourse().getId(), DateUtil.getYear(new Date()) + 1);
                Date nextExpiry = DateUtil.getDate("30-06-" + (renewalInvDetail.getYear() + 1));
                StudentCourseSubscription subscription = new StudentCourseSubscription(DateUtil.getYear(new Date()) + 1, invoice.getStudentCourse());
                subscription.setPk(pk);
                subscription.setExpiry(nextExpiry);
                subscription.setInvoice(invoice);
                super.edit(subscription);
            }
        }
    }

    public void createSubscription(RenewalInvoiceDetail invoiceDetail) throws ParseException {
        Date nextExpiry = DateUtil.getDate("30-06-" + (invoiceDetail.getYear() + 1));
        StudentCourseSubscriptionPK pk = new StudentCourseSubscriptionPK(invoiceDetail.getInvoice().getStudentCourse().getId(), DateUtil.getYear(new Date()) + 1);
        StudentCourseSubscription subscription = new StudentCourseSubscription(DateUtil.getYear(new Date()) + 1, invoiceDetail.getInvoice().getStudentCourse());
        subscription.setPk(pk);
        subscription.setExpiry(nextExpiry);
        super.edit(subscription);
    }

}
