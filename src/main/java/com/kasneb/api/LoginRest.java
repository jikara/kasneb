/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.Login;
import com.kasneb.exception.CustomMessage;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.SecurityUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final String STUDENT_KEY = "C742F13F-F981-4835-A9FA-D5A9EC21C3EF";
    private static final String ADMIN_KEY = "75D8CFBA-7C03-42E0-A22B-627E1349CBEA";

    @EJB
    com.kasneb.session.LoginFacade loginFacade;

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Status httpStatus = Status.INTERNAL_SERVER_ERROR;
    String json;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpHeaders headers, Login login) throws JsonProcessingException {
        String token = null;
        int loginAttempts = 0;
        try {
            if (headers.getRequestHeader("App-Key") == null || headers.getRequestHeader("App-Key").get(0).isEmpty()) {
                throw new CustomHttpException(Response.Status.FORBIDDEN, "No app key header present");
            }
            String appKey = headers.getRequestHeader("App-Key").get(0);
            switch (appKey) {
                case STUDENT_KEY:
                    login = loginFacade.loginStudent(login);
                    break;
                case ADMIN_KEY:
                    login = loginFacade.loginUser(login);
                    break;
                default:
                    throw new CustomHttpException(Response.Status.FORBIDDEN, "Invalid app key");
            }
            httpStatus = Status.OK;
            anyResponse = login;
            token = SecurityUtil.createJWT(login.getId(), "Kasneb", "Authorization Token", 1000000);
            login.setLoginAttempts(0);
        } catch (CustomHttpException ex) {
            loginAttempts = loginAttempts + login.getLoginAttempts();
            login.setLoginAttempts(loginAttempts);
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .header("Authorization", token)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Integer id) throws JsonProcessingException {
        anyResponse = loginFacade.find(id);
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

}
