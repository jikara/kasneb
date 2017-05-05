/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.model;

/**
 *
 * @author jikara
 */
public class ErrorResponse {

    private String ErrorType;
    private Integer ErrorCode;
    private String Message;
    private String error_description;

    public ErrorResponse() {
    }

    public String getErrorType() {
        return ErrorType;
    }

    public void setErrorType(String ErrorType) {
        this.ErrorType = ErrorType;
    }

    public Integer getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(Integer ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
