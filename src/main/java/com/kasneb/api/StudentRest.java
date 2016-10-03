/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.Student;
import com.kasneb.entity.Login;
import com.kasneb.exception.CustomMessage;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.Email;
import com.kasneb.util.EmailUtil;
import com.kasneb.util.SecurityUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.mvc.Template;
import org.glassfish.jersey.server.mvc.Viewable;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("student")
public class StudentRest {
    
    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.LoginFacade loginFacade;

    /**
     * Creates a new instance of StudentRest
     */
    public StudentRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.StudentRest
     *
     * @return an instance of javax.ws.rs.core.Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() throws JsonProcessingException {
        anyResponse = studentFacade.findAll();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }
    
    @GET
    @Path("pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPendingAll() throws JsonProcessingException {
        anyResponse = studentFacade.findAll();
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     *
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id) throws JsonProcessingException {
        anyResponse = studentFacade.find(id);
        json = mapper.writeValueAsString(anyResponse);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of StudentRest
     *
     * @param entity
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Student entity) throws JsonProcessingException {
        try {
            Login loginId = studentFacade.find(entity.getId()).getLoginId();
            System.out.println(entity.toString());
            entity.setLoginId(loginId);
            entity = studentFacade.edit(entity);
            anyResponse = entity;
            httpStatus = Response.Status.OK;
        } catch (Exception ex) {
            anyResponse = new CustomMessage(500, ex.getMessage());
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     *
     * @param entity
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Student entity) throws JsonProcessingException {
        Map emailProps = new HashMap<>();
        try {
            entity = studentFacade.createStudent(entity);
            String key = SecurityUtil.createJWT(entity.getId(), "Kasneb", "Verification Key", 1000 * 60 * 60 * 24);
            entity.getLoginId().setVerificationToken(key);
            entity.getLoginId().setStudent(entity);
            entity = studentFacade.edit(entity);
            emailProps.put("login", entity.getLoginId());
            EmailUtil.sendEmail(new Email(entity.getLoginId().getEmail(), "Account Verification", emailProps));
            anyResponse = entity;
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
    
    @POST
    @Path("resend")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response resendEmail(Login entity) throws JsonProcessingException {
        Map emailProps = new HashMap<>();
        try {
            Login managed = loginFacade.findByEmail(entity.getEmail());
            if (managed == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Account does not exist");
            }
            if (managed.getActivated()) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Account already activated");
            }
            emailProps.put("login", managed);
            EmailUtil.sendEmail(new Email(managed.getEmail(), "Account Verification", emailProps));//Send verification email
            httpStatus = Response.Status.OK;
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), "Verification email resent successfully ");
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
    
    @GET
    @Path("invoices/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response invoices(@PathParam("id") Integer id) throws JsonProcessingException {
        anyResponse = studentFacade.findInvoices(id);
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }
    
    @PUT
    @Path("verify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verify(Login login) throws JsonProcessingException {
        try {
            studentFacade.verifyAccount(login.getVerificationToken());
            httpStatus = Response.Status.OK;
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), "Account successfully verified");
        } catch (CustomHttpException e) {
            httpStatus = e.getStatusCode();
            anyResponse = new CustomMessage(e.getStatusCode().getStatusCode(), e.getMessage());
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, e);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
    
    @GET
    @Path("email")
    @Template
    @Produces({MediaType.TEXT_PLAIN})
    public Viewable get() {
        return new Viewable("index.foo", "FOO");
    }
    
}
