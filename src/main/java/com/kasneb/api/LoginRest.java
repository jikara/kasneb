/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.kasneb.entity.Login;
import com.kasneb.exception.CustomMessage;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.Constants;
import com.kasneb.util.PredicateUtil;
import com.kasneb.util.SecurityUtil;
import java.io.IOException;
import java.text.ParseException;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author jikara
 */
@Path("login")
public class LoginRest {

    @EJB
    com.kasneb.session.LoginFacade loginFacade;
    Object anyResponse = new Object();
    Status httpStatus = Status.INTERNAL_SERVER_ERROR;

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Integer id) {
        anyResponse = loginFacade.find(id);

        return Response
                .status(Response.Status.OK)
                .entity(anyResponse)
                .build();
    }

    @POST
    @Path("send_token")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendResetToken(@Context HttpHeaders headers, Login entity) {
        String token = null;
        int loginAttempts = 0;
        try {
            if (headers.getRequestHeader("App-Key") == null || headers.getRequestHeader("App-Key").get(0).isEmpty()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No app key header present");
            }
            String appKey = headers.getRequestHeader(Constants.DEVICE_HEADER_NAME).get(0);
            if (PredicateUtil.isSet(appKey) && appKey.equals(Constants.MOBILE_APP_KEY)) {
                loginFacade.sendStudentSmsResetToken(entity);
            } else if (PredicateUtil.isSet(appKey) && appKey.equals(Constants.WEB_APP_KEY)) {
                loginFacade.sendStudentEmailResetToken(entity);
            } else {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid app key");
            }
            httpStatus = Status.OK;
            token = SecurityUtil.createJWT(entity.getId(), "Kasneb", "Authorization Token", 1000000);
            entity.setLoginAttempts(0);
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .header("Authorization", token)
                .build();
    }

    @POST
    @Path("reset_password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response resetPassword(@Context HttpHeaders headers, Login entity) {
        String token = null;
        int loginAttempts = 0;
        try {
            if (headers.getRequestHeader("App-Key") == null || headers.getRequestHeader("App-Key").get(0).isEmpty()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No app key header present");
            }
            String appKey = headers.getRequestHeader(Constants.DEVICE_HEADER_NAME).get(0);

            switch (appKey) {
                case Constants.STUDENT_KEY:
                    entity = loginFacade.resetStudentPasswordEmail(entity);
                    break;
                case Constants.ADMIN_KEY:
                    //loginFacade.resetUserPassword(login);
                    break;
                case Constants.MOBILE_APP_KEY:
                    entity = loginFacade.resetStudentPasswordSms(entity);
                    break;
                default:
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid app key");
            }
            httpStatus = Status.OK;
            token = SecurityUtil.createJWT(entity.getId(), "Kasneb", "Authorization Token", 1000000);
            entity.setLoginAttempts(0);
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .header("Authorization", token)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Login entity, @Context HttpHeaders headers) {
        String token = null;
        int loginAttempts = 0;
        try {
            if (headers.getRequestHeader("App-Key") == null || headers.getRequestHeader("App-Key").get(0).isEmpty()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No app key header present");
            }
            String appKey = headers.getRequestHeader("App-Key").get(0);
            switch (appKey) {
                case Constants.STUDENT_KEY:
                    entity = loginFacade.loginStudent(entity);
                    break;
                case Constants.ADMIN_KEY:
                    entity = loginFacade.loginUser(entity);
                    break;
                default:
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid app key");
            }
            httpStatus = Status.OK;
            token = SecurityUtil.createJWT(entity.getId(), "Kasneb", "Authorization Token", 1000 * 60 * 60 * 24 * 367);
            entity.setLoginAttempts(0);
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
        } catch (IOException | ParseException ex) {
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .header("Authorization", token)
                .build();
    }

}
