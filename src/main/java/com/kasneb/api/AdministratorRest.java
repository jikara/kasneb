/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kasneb.entity.Declaration;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.Institution;
import com.kasneb.entity.OtherCourse;
import com.kasneb.entity.User;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import com.kasneb.util.DateUtil;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("administrator")
public class AdministratorRest {

    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @Context
    private UriInfo context;
    @EJB
    com.kasneb.session.UserFacade userFacade;
    @EJB
    com.kasneb.session.RoleFacade roleFacade;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;
    @EJB
    com.kasneb.session.DeclarationFacade declarationFacade;
    @EJB
    com.kasneb.session.OtherCourseFacade otherCourseFacade;
    @EJB
    com.kasneb.session.InstitutionFacade institutionFacade;
    @EJB
    com.kasneb.session.PaymentFacade paymentFacade;
    @EJB
    com.kasneb.session.AuditTrailFacade auditTrailFacade;

    /**
     * Creates a new instance of AdministratorRest
     */
    public AdministratorRest() {
    }

    @GET
    @Path("students")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudents(@QueryParam("searchKey") String searchKey) {
        try {
            anyResponse = studentFacade.findAll();
            httpStatus = Response.Status.OK;
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("student/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") Integer id) {
        try {
            anyResponse = studentFacade.find(id);
            httpStatus = Response.Status.OK;
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() {
        try {
            anyResponse = userFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Integer id) {
        try {
            anyResponse = userFacade.find(id);
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("roles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles() {
        try {
            anyResponse = roleFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("role/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRole(@PathParam("id") Integer id) {
        try {
            anyResponse = roleFacade.find(id);
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.AdministratorRest
     *
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Path("studentcourse/verified")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVerifiedStudentCourses() {
        try {
            anyResponse = studentCourseFacade.findVerified();
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("studentcourse/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnverifiedStudentCourses() {
        try {
            anyResponse = studentCourseFacade.findPending();
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("exam/booking")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExamBookings() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("exam/booking/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExamBookings(@PathParam("id") Integer sittingId) {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("payment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPayments() {
        try {
            anyResponse = paymentFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("payment/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPayments(@PathParam("code") String feeCode) {
        try {
            anyResponse = paymentFacade.findByCode(feeCode);
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("studentcourse/verifiedby/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentCourseVerifications(@PathParam("id") Integer userId) {
        try {
            User user = userFacade.find(userId);
            if (user == null) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "User does not exist");
            }
            anyResponse = studentCourseFacade.findVerificationByUser(user);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            json = mapper.writeValueAsString(anyResponse);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("payment/summary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentSummary(@QueryParam("feeCode") String code, @QueryParam("startDate") String fromDate, @QueryParam("endDate") String toDate) throws JsonProcessingException {
        boolean dateRange = true;
        Date startDate = null, endDate = null;
        try {
            if (fromDate == null || toDate == null) {
                dateRange = false;
            }
            if (dateRange) {
                try {
                    startDate = DateUtil.getDate(fromDate);
                    endDate = DateUtil.getToDate(toDate);
                } catch (ParseException ex) {
                    Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid date format");
                }
            }
            if (dateRange && code == null) {
                anyResponse = paymentFacade.getPaymentSummary(startDate, endDate);
            } else if (dateRange && code != null) {
                FeeCode feeCode = new FeeCode(code);
                anyResponse = paymentFacade.getPaymentSummary(feeCode, startDate, endDate);
            } else if (code != null && !dateRange) {
                FeeCode feeCode = new FeeCode(code);
                anyResponse = paymentFacade.getPaymentSummary(feeCode);
            } else if (code == null && !dateRange) {
                anyResponse = paymentFacade.getPaymentSummary();
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("studentcourse")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentCourses() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("exemption/other")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOtherCourseExemptions() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("declaration")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getOtherCourseExemptions(Declaration declaration) {
        try {
            anyResponse = declarationFacade.create(declaration);
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test() {
        try {
            anyResponse = otherCourseFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @POST
    @Path("qualification/other")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOtherCourseType(String json) {
        try {
            OtherCourse entity = mapper.convertValue(json, OtherCourse.class);
            anyResponse = otherCourseFacade.create(entity);
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @POST
    @Path("institution")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addInstitution(Institution institution) {
        try {
            anyResponse = institutionFacade.create(institution);
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("audittrail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            anyResponse = auditTrailFacade.findAll();
            json = mapper.writeValueAsString(anyResponse);
            httpStatus = Response.Status.OK;
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("audittrail/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByUser(@PathParam("id") Integer userId) throws JsonProcessingException {
        try {
            anyResponse = auditTrailFacade.findByUser(userId);
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();

    }
}
