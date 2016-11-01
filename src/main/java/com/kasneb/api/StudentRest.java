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
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomMessage;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.Email;
import com.kasneb.model.Sms;
import com.kasneb.util.EmailUtil;
import com.kasneb.util.SecurityUtil;
import com.kasneb.util.SmsUtil;
import java.math.BigDecimal;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
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
    
    private static final String DEVICE_HEADER_NAME = "Client-Id";

    /**
     * Creates a new instance of StudentRest
     */
    public StudentRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.StudentRest
     *
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            anyResponse = studentFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }
    
    @GET
    @Path("pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPendingAll() {
        try {
            anyResponse = studentFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    /**
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id) {
        try {
            Student student = studentFacade.find(id);
            for (StudentCourse course : student.getStudentCourses()) {
                if (course.getActive()) {
                    course.setStudent(new Student(course.getStudent().getId()));
                    student.setCurrentCourse(course);
                }
            }
            anyResponse = student;
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Student entity) {
        try {
            entity = studentFacade.updateStudent(entity);
            anyResponse = entity;
            httpStatus = Response.Status.OK;
        } catch (Exception ex) {
            anyResponse = new CustomMessage(500, ex.getMessage());
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     *
     * @param headers
     * @param entity
     * @return
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Context HttpHeaders headers, Student entity) {
        Map emailProps = new HashMap<>();
        try {
            entity = studentFacade.createStudent(entity);
            String key = SecurityUtil.createJWT(entity.getId(), "Kasneb", "Verification Key", 1000 * 60 * 60 * 24);
            String smsToken = SecurityUtil.createSmsToken(entity.getId());
            entity.getLoginId().setVerificationToken(key);
            entity.getLoginId().setSmsToken(smsToken);
            entity.getLoginId().setStudent(entity);
            entity = studentFacade.edit(entity);
            if (headers.getRequestHeader(DEVICE_HEADER_NAME) != null && !headers.getRequestHeader(DEVICE_HEADER_NAME).get(0).isEmpty()) {
                //Send SMS
                SmsUtil.sendSMS(new Sms(entity.getPhoneNumber(), smsToken));
            } else {
                //Send Email                
                String body = "Dear " + entity.getFirstName() + " " + entity.getMiddleName() + ",<br>\n"
                        + "Thank you for signing up to the KASNEB student portal. Please click below link to verify your account:<br><a href='http://192.168.11.211:81/studentsportal/site/activate-account?token=" + key + "'>Link<a><br>Your Kasneb wallet pin is 1234, to change the pin. Go to https://www.jambopay.com/. Sign in to the portal and navigate to change Password menu and change your pin.";
                EmailUtil.sendEmail(new Email(entity.getLoginId().getEmail(), "Account Verification", body));
            }
            anyResponse = entity;
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

//    @POST
//    @Path("resend")
//    @Consumes({MediaType.APPLICATION_JSON})
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response resendEmail(Login entity) {
//        Map emailProps = new HashMap<>();
//        try {
//            Login managed = loginFacade.findByEmail(entity.getEmail());
//            if (managed == null) {
//                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Account does not exist");
//            }
//            if (managed.getActivated()) {
//                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Account already activated");
//            }
// //Send Email                
//                String body = "Dear Studentname,<br>\n"
//                        + "Thank you for signing up to the KASNEB student portal. Please click below link to verify your account:<br><a href='http://192.168.11.211:81/studentsportal/site/activate-account?token=" + key + "'>Link<a><br>Your Kasneb wallet pin is 1234, to change the pin. Go to https://www.jambopay.com/. Sign in to the portal and navigate to change Password menu and change your pin.";
//                EmailUtil.sendEmail(new Email(entity.getLoginId().getEmail(), "Account Verification", body));
//            httpStatus = Response.Status.OK;
//            anyResponse = new CustomMessage(httpStatus.getStatusCode(), "Verification email resent successfully ");
//        } catch (CustomHttpException ex) {
//            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
//            httpStatus = ex.getStatusCode();
//            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (MessagingException ex) {
//            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        try {
//            json = mapper.writeValueAsString(anyResponse);
//        } catch (JsonProcessingException ex) {
//            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return Response
//                .status(httpStatus)
//                .entity(json)
//                .build();
//    }
    @GET
    @Path("invoices/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response invoices(@PathParam("id") Integer id) {
        try {
            anyResponse = studentFacade.findInvoices(id);
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }
    
    @PUT
    @Path("verify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verify(@Context HttpHeaders headers, Login login) {
        try {
            if (headers.getRequestHeader(DEVICE_HEADER_NAME) != null && !headers.getRequestHeader(DEVICE_HEADER_NAME).get(0).isEmpty()) {
                //Send SMS
                studentFacade.verifySmsAccount(login.getSmsToken());
            } else {
                studentFacade.verifyAccount(login.getVerificationToken());
            }
            httpStatus = Response.Status.OK;
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), "Account successfully verified");
        } catch (CustomHttpException e) {
            httpStatus = e.getStatusCode();
            anyResponse = new CustomMessage(e.getStatusCode().getStatusCode(), e.getMessage());
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    @POST
    @Path("balance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBalance(Student student) {
        try {
            BigDecimal balance = studentFacade.getBalance(student);
            httpStatus = Response.Status.OK;
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), "Account balance is Ksh " + balance);
        } catch (CustomHttpException e) {
            httpStatus = e.getStatusCode();
            anyResponse = new CustomMessage(e.getStatusCode().getStatusCode(), e.getMessage());
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
    
}
