/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author jikara
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.kasneb.api.AdministratorRest.class);
        resources.add(com.kasneb.api.CentreZoneRest.class);
        resources.add(com.kasneb.api.CoreRest.class);
        resources.add(com.kasneb.api.CountryRest.class);
        resources.add(com.kasneb.api.CountyRest.class);
        resources.add(com.kasneb.api.CourseExemptionRest.class);
        resources.add(com.kasneb.api.CourseRest.class);
        resources.add(com.kasneb.api.CourseTypeRest.class);
        resources.add(com.kasneb.api.DeclarationRest.class);
        resources.add(com.kasneb.api.DocumentTypeRest.class);
        resources.add(com.kasneb.api.ExamCentreRest.class);
        resources.add(com.kasneb.api.ExemptionRest.class);
        resources.add(com.kasneb.api.ExportRest.class);
        resources.add(com.kasneb.api.FeeCodeRest.class);
        resources.add(com.kasneb.api.FeeTypeRest.class);
        resources.add(com.kasneb.api.GuideRest.class);
        resources.add(com.kasneb.api.InstitutionRest.class);
        resources.add(com.kasneb.api.InvoiceRest.class);
        resources.add(com.kasneb.api.LoginRest.class);
        resources.add(com.kasneb.api.NotificationRest.class);
        resources.add(com.kasneb.api.PaymentRest.class);
        resources.add(com.kasneb.api.QualificationRest.class);
        resources.add(com.kasneb.api.RenewalRest.class);
        resources.add(com.kasneb.api.ResourceRest.class);
        resources.add(com.kasneb.api.RoleRest.class);
        resources.add(com.kasneb.api.SittingRest.class);
        resources.add(com.kasneb.api.StudentCourseRest.class);
        resources.add(com.kasneb.api.StudentCourseSittingRest.class);
        resources.add(com.kasneb.api.StudentDeclarationRest.class);
        resources.add(com.kasneb.api.StudentRest.class);
        resources.add(com.kasneb.api.TestRest.class);
        resources.add(com.kasneb.api.TransactionRest.class);
        resources.add(com.kasneb.api.UserRest.class);
    }

}
