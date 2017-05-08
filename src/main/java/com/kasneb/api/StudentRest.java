/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.entity.AlertType;
import com.kasneb.entity.Communication;
import com.kasneb.entity.CommunicationType;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.Student;
import com.kasneb.entity.Login;
import com.kasneb.entity.StudentCourse;
import com.kasneb.exception.CustomMessage;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.Constants;
import com.kasneb.util.PredicateUtil;
import com.kasneb.util.SecurityUtil;
import com.kasneb.util.WalletUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("student")
@Stateless
public class StudentRest {

    @Context
    ServletContext servletContext;
    @Context
    HttpServletRequest request;
    Object anyResponse = new Object();
    ObjectMapper mapper = new ObjectMapper();
    Hibernate5Module hbm = new Hibernate5Module();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.LoginFacade loginFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;
    @EJB
    com.kasneb.session.CommunicationFacade communicationFacade;

    /**
     * Creates a new instance of StudentRest
     */
    public StudentRest() {
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
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
        List<Student> students = studentFacade.findAll();
        json = mapper.writeValueAsString(students);
        return Response
                .status(Response.Status.OK)
                .entity(json)
                .build();
    }

    @GET
    @Path("pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPendingAll() {
        List<Student> students = studentFacade.findAll();
        return Response
                .status(Response.Status.OK)
                .entity(students)
                .build();
    }

    /**
     *
     * @param id
     * @return
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id) throws JsonProcessingException {
        mapper.registerModule(hbm);
        Student student = studentFacade.find(id);
        for(StudentCourse studentCourse:student.getStudentCourses()){
            studentCourse.getDocuments();
            studentCourse.getStudentRequirements();
        }
        json = mapper.writeValueAsString(student);
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
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Student entity) {
        try {
            entity = studentFacade.updateStudent(entity);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException | IllegalAccessException | InvocationTargetException ex) {
            anyResponse = new CustomMessage(500, ex.getMessage());
            httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
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
        try {
            if (entity.getStudentStatus() != null && entity.getStudentStatus() == 2) {
//                if (studentCourseFacade.registrationExists(entity) ) {
//                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Student already registered for this course");
//                }
                Student existing = studentFacade.verifyPreviousStudentCourse(entity);
                //Copy non null prperties
                studentFacade.copy(existing, entity);
            }
            entity.getLoginId().setPhoneNumber(entity.getPhoneNumber());
            entity = studentFacade.createStudent(entity);
            String key = SecurityUtil.createJWT(entity.getId(), "Kasneb", "Verification Key", 1000 * 60 * 60 * 24 * 367);
            String smsToken = SecurityUtil.createSmsVerificationToken(entity.getId());
            entity.getLoginId().setVerificationToken(key);
            entity.getLoginId().setSmsToken(smsToken);
            entity.getLoginId().setStudent(entity);
            entity = studentFacade.edit(entity);
            //Log Communication
            Communication communication = new Communication(entity, null, null, null, CommunicationType.ACCOUNT_VERIFICATION, AlertType.EMAIL, false);
            communicationFacade.create(communication);
            String appKey = headers.getRequestHeader(Constants.DEVICE_HEADER_NAME).get(0);
            if (PredicateUtil.isSet(appKey) && appKey.equals(Constants.MOBILE_APP_KEY)) {
                communication = new Communication(entity, null, null, null, CommunicationType.SMS_VERIFICATION, AlertType.SMS, false);
                communicationFacade.create(communication);
            }
            if (!WalletUtil.walletExists(entity.getPhoneNumber())) {
                communication = new Communication(entity, null, null, null, CommunicationType.WALLET_DETAIL, AlertType.SMS, false);
                communicationFacade.create(communication);
                communication.setAlertType(AlertType.EMAIL);
                communicationFacade.create(communication);
            } else {
                communication = new Communication(entity, null, null, null, CommunicationType.WALLET_DETAIL_EXISTING, AlertType.EMAIL, false);
                communicationFacade.create(communication);
                communication.setAlertType(AlertType.EMAIL);
                communicationFacade.create(communication);
            }
            anyResponse = entity;
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ParseException | IllegalAccessException | InvocationTargetException ex) {
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), ex.getMessage());
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @GET
    @Path("invoices/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response invoices(@PathParam("id") Integer id) {
        List<Invoice> invoices = studentFacade.findInvoices(id);
        return Response
                .status(Response.Status.OK)
                .entity(invoices)
                .build();
    }

    @PUT
    @Path("verify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verify(@Context HttpHeaders headers, Login entity) {
        try {
            if (headers.getRequestHeader(Constants.DEVICE_HEADER_NAME) != null && !headers.getRequestHeader(Constants.DEVICE_HEADER_NAME).get(0).isEmpty()) {
                String appKey = headers.getRequestHeader(Constants.DEVICE_HEADER_NAME).get(0);
                if (PredicateUtil.isSet(appKey) && appKey.equals(Constants.MOBILE_APP_KEY)) {
                    //Veirfy SMS
                    studentFacade.verifyPhoneNumber(entity.getSmsToken());
                } else {
                    //Verify EMAIL
                    studentFacade.verifyEmail(entity.getVerificationToken());
                }
            } else {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "No app-key header");
            }
            httpStatus = Response.Status.OK;
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), "Account successfully verified");
        } catch (CustomHttpException e) {
            httpStatus = e.getStatusCode();
            anyResponse = new CustomMessage(e.getStatusCode().getStatusCode(), e.getMessage());
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, e);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @POST
    @Path("balance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response getBalance(Student entity) {
        try {
            BigDecimal balance = studentFacade.getBalance(entity);
            httpStatus = Response.Status.OK;
            anyResponse = new CustomMessage(httpStatus.getStatusCode(), "Your current Kasneb wallet balance is Ksh " + balance);
        } catch (CustomHttpException e) {
            httpStatus = e.getStatusCode();
            anyResponse = new CustomMessage(e.getStatusCode().getStatusCode(), e.getMessage());
            // Logger.getLogger(StudentRest.class.getName()).log(Level.SEVERE, null, e);
        }
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

}
