/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.bean;

import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jikara
 */
@Named(value = "constantBean")
@ApplicationScoped
public class ConstantBean {

    private String baseUrl;
    private String portalUrl;

    /**
     * Creates a new instance of NewJSFManagedBean
     */
    public ConstantBean() {
    }

    public String getBaseUrl() {
        HttpServletRequest httpRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        StringBuffer url = httpRequest.getRequestURL();
        String uri = httpRequest.getRequestURI();
        String ctx = httpRequest.getContextPath();
        baseUrl = "http://online.kasneb.or.ke:8080/kasneb/";// url.substring(0, url.length() - uri.length() + ctx.length()) + "/";
        return baseUrl;
    }

    public String getPortalUrl() {
        portalUrl = "http://online.kasneb.or.ke/";// url.substring(0, url.length() - uri.length() + ctx.length()) + "/";
        return portalUrl;
    }

}
