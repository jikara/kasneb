/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.bean;

import com.kasneb.entity.Communication;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author jikara
 */
@Named(value = "email")
@RequestScoped
public class Email implements Serializable {

    @EJB
    com.kasneb.session.CommunicationFacade comunicationFacade;

    private Communication communication;
    private Integer communicationId;
    private String pin;

    /**
     * Creates a new instance of Email
     */
    public Email() {
    }

    public void setCommunicationId(Integer communicationId) {
        this.communicationId = communicationId;
    }

    public Integer getCommunicationId() {
        return communicationId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Communication getCommunication() {
        communication = comunicationFacade.find(communicationId);
        return communication;
    }

    public void setCommunication(Communication communication) {
        this.communication = communication;
    }

}
