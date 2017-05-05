/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.exception;

import javax.ws.rs.core.Response.Status;

/**
 *
 * @author jikara
 */
public class CustomHttpException extends Exception {

    private Status statusCode;

    /**
     * Creates a new instance of <code>CustomHttpException</code> without detail
     * message.
     */
    public CustomHttpException() {
    }

    /**
     * Constructs an instance of <code>CustomHttpException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomHttpException(String msg) {
        super(msg);
    }

    public CustomHttpException(Status statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public Status getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Status statusCode) {
        this.statusCode = statusCode;
    }
}
