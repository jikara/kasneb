/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.entity.Declaration;
import com.kasneb.entity.Exemption;
import com.kasneb.entity.FeeCode;
import com.kasneb.entity.Institution;
import com.kasneb.entity.OtherCourse;
import com.kasneb.entity.Payment;
import com.kasneb.entity.Role;
import com.kasneb.entity.SittingPeriod;
import com.kasneb.entity.Student;
import com.kasneb.entity.StudentCourse;
import com.kasneb.entity.User;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import com.kasneb.util.DateUtil;
import com.kasneb.util.PredicateUtil;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.apache.commons.lang.time.DateUtils.addDays;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("administrator")
@Stateless
public class AdministratorRest {

    @Context
    private UriInfo context;
    Object anyResponse = new Object();
    ObjectMapper mapper = new ObjectMapper();
    Hibernate5Module hbm = new Hibernate5Module();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.UserFacade userFacade;
    @EJB
    com.kasneb.session.RoleFacade roleFacade;
    @EJB
    com.kasneb.session.StudentFacade studentFacade;
    @EJB
    com.kasneb.session.StudentCourseFacade studentCourseFacade;
    @EJB
    com.kasneb.session.StudentCourseSittingFacade studentCourseSittingFacade;
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
    @EJB
    com.kasneb.session.ExemptionFacade exemptionFacade;

    /**
     * Creates a new instance of AdministratorRest
     */
    public AdministratorRest() {
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    @GET
    @Path("student/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudent(@PathParam("id") Integer id) throws JsonProcessingException {
        Student student = studentFacade.find(id);
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(student);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() throws JsonProcessingException {
        List<User> users = userFacade.findAll();
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(users);
        return Response
                .status(httpStatus)
                .entity(users)
                .build();
    }

    @GET
    @Path("user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") Integer id) throws JsonProcessingException {
        User user = userFacade.find(id);
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(user);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("roles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles() throws JsonProcessingException {
        List<Role> roles = roleFacade.findAll();
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(roles);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("role/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRole(@PathParam("id") Integer id) throws JsonProcessingException {
        Role role = roleFacade.find(id);
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(role);
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
    public Response getVerifiedStudentCourses() throws JsonProcessingException {
        List<StudentCourse> studentCourses = studentCourseFacade.findVerified();
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(studentCourses);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("studentcourse/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnverifiedStudentCourses() throws JsonProcessingException {
        List<StudentCourse> studentCourses = studentCourseFacade.findPending();
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(studentCourses);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("studentcourse/verification")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentCourseVerifications(@QueryParam("from") String fromDate, @QueryParam("to") String toDate, @QueryParam("userId") Integer userId) throws JsonProcessingException {
        List<StudentCourse> verifications = new ArrayList<>();
        boolean dateRange = true;
        Date startDate = null, endDate = null;
        try {
            if (dateRange) {
                try {
                    startDate = DateUtil.getDate(fromDate);
                    endDate = DateUtil.getDate(toDate);
                    endDate = addDays(endDate, 1);
                } catch (ParseException ex) {
                    //Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid date format");
                }
            }
            if (userId == null) {
                verifications = studentCourseFacade.findVerifications(userId);
            } else {
                verifications = studentCourseFacade.findVerifications(userId, startDate, endDate);
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            //Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(verifications);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("exemption/verification")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExemptionVerifications(@QueryParam("from") String fromDate, @QueryParam("to") String toDate, @QueryParam("userId") Integer userId) throws JsonProcessingException {
        List<Exemption> exemptions = new ArrayList<>();
        boolean dateRange = true;
        Date startDate = null, endDate = null;
        try {
            if (dateRange) {
                try {
                    startDate = DateUtil.getDate(fromDate);
                    endDate = DateUtil.getDate(toDate);
                    endDate = addDays(endDate, 1);
                } catch (ParseException ex) {
                    //Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid date format");
                }
            }
            if (userId == null) {
                exemptions = exemptionFacade.findAll();
            } else {
                exemptions = exemptionFacade.findAll(userId, startDate, endDate);
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(exemptions);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("payment/summary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentSummary(@QueryParam("feeCode") String code, @QueryParam("startDate") String fromDate, @QueryParam("endDate") String toDate) throws JsonProcessingException {
        Collection<Payment> payments = new ArrayList<>();
        boolean dateRange = true;
        Date startDate = null, endDate = null;
        try {
            if (code != null && code.trim().equals("")) {
                code = null;
            }
            if (fromDate == null || toDate == null) {
                dateRange = false;
            }
            if (dateRange) {
                try {
                    startDate = DateUtil.getDate(fromDate);
                    endDate = DateUtil.getDate(toDate);
                    endDate = addDays(endDate, 1);
                } catch (ParseException ex) {
                    // Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
                    throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "Invalid date format");
                }
            }
            if (dateRange && code == null) {
                payments = paymentFacade.getPaymentSummary(startDate, endDate);
            } else if (dateRange && code != null) {
                FeeCode feeCode = new FeeCode(code);
                payments = paymentFacade.getPaymentSummary(feeCode, startDate, endDate);
            } else if (code != null && !dateRange) {
                FeeCode feeCode = new FeeCode(code);
                payments = paymentFacade.getPaymentSummary(feeCode);
            } else if (code == null && !dateRange) {
                payments = paymentFacade.getPaymentSummary();
            }
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            httpStatus = ex.getStatusCode();
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            // Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(payments);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("payment")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPayments(@QueryParam("feeCode") String code, @QueryParam("startDate") String fromDate, @QueryParam("endDate") String toDate) {
        boolean dateRange = true;
        Date startDate = null, endDate = null;
        try {
            if (code != null && code.trim().equals("")) {
                code = null;
            }
            if (fromDate == null || toDate == null) {
                dateRange = false;
            }
            if (dateRange) {
                try {
                    startDate = DateUtil.getDate(fromDate);
                    endDate = DateUtil.getDate(toDate);
                    endDate = addDays(endDate, 1);
                } catch (ParseException ex) {
                    // Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
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
            // Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("studentcoursesitting")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentCourseSittings(@QueryParam("courseTypeCode") String scourseTypeCode, @QueryParam("courseId") String courseId, @QueryParam("year") String syear, @QueryParam("month") String smonth) {
        if (PredicateUtil.isSet(scourseTypeCode) && PredicateUtil.isSet(courseId) && PredicateUtil.isSet(syear) && PredicateUtil.isSet(smonth)) { //All set
            anyResponse = studentCourseSittingFacade.findAll(Integer.parseInt(scourseTypeCode), courseId, Integer.parseInt(syear), SittingPeriod.valueOf(smonth));
        } else if (PredicateUtil.isSet(scourseTypeCode) && !PredicateUtil.isSet(courseId) && !PredicateUtil.isSet(syear) && !PredicateUtil.isSet(smonth)) { //only courseType code set
            anyResponse = studentCourseSittingFacade.findAll(Integer.parseInt(scourseTypeCode));
        } else if (!PredicateUtil.isSet(scourseTypeCode) && PredicateUtil.isSet(courseId) && !PredicateUtil.isSet(syear) && !PredicateUtil.isSet(smonth)) { ////only course set
            anyResponse = studentCourseSittingFacade.findAll(courseId);
        } else if (!PredicateUtil.isSet(scourseTypeCode) && !PredicateUtil.isSet(courseId) && PredicateUtil.isSet(syear) && !PredicateUtil.isSet(smonth)) { //Only year set
            anyResponse = studentCourseSittingFacade.findAllByYear(Integer.parseInt(syear));
        } else if (!PredicateUtil.isSet(scourseTypeCode) && !PredicateUtil.isSet(courseId) && !PredicateUtil.isSet(syear) && PredicateUtil.isSet(smonth)) { //Only month set
            anyResponse = studentCourseSittingFacade.findAll(SittingPeriod.valueOf(smonth));
        } else if (!PredicateUtil.isSet(scourseTypeCode) && !PredicateUtil.isSet(courseId) && PredicateUtil.isSet(syear) && !PredicateUtil.isSet(smonth)) { //Course type and year set
            anyResponse = studentCourseSittingFacade.findAll(Integer.parseInt(scourseTypeCode), Integer.parseInt(syear));
        } else if (!PredicateUtil.isSet(scourseTypeCode) && !PredicateUtil.isSet(courseId) && !PredicateUtil.isSet(syear) && PredicateUtil.isSet(smonth)) { //Course type and month set
            anyResponse = studentCourseSittingFacade.findAll(Integer.parseInt(scourseTypeCode), SittingPeriod.valueOf(smonth));
        } else if (!PredicateUtil.isSet(scourseTypeCode) && PredicateUtil.isSet(courseId) && PredicateUtil.isSet(syear) && PredicateUtil.isSet(smonth)) { //Course and year and month set
            anyResponse = studentCourseSittingFacade.findAll(courseId, Integer.parseInt(syear), SittingPeriod.valueOf(smonth));
        } else if (!PredicateUtil.isSet(scourseTypeCode) && PredicateUtil.isSet(courseId) && !PredicateUtil.isSet(syear) && PredicateUtil.isSet(smonth)) { //Course  and month set
            anyResponse = studentCourseSittingFacade.findAll(courseId, SittingPeriod.valueOf(smonth));
        } else if (!PredicateUtil.isSet(scourseTypeCode) && !PredicateUtil.isSet(courseId) && PredicateUtil.isSet(syear) && PredicateUtil.isSet(smonth)) { //Year and month set
            anyResponse = studentCourseSittingFacade.findAllByYear(Integer.parseInt(syear), SittingPeriod.valueOf(smonth));
        } else if (PredicateUtil.isSet(scourseTypeCode) && !PredicateUtil.isSet(courseId) && PredicateUtil.isSet(syear) && PredicateUtil.isSet(smonth)) { //CourseType and Year and month set
            anyResponse = studentCourseSittingFacade.findAll(Integer.parseInt(scourseTypeCode), Integer.parseInt(syear), SittingPeriod.valueOf(smonth));
        } else {
            anyResponse = studentCourseSittingFacade.findAll();
        }
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("studentcourse")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentCourses(@QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("userId") Integer userId) throws ParseException, JsonProcessingException {
        Date startDate = null, endDate = null;
        boolean dateRange = false;
        if (PredicateUtil.isSet(from) && PredicateUtil.isSet(to)) {
            dateRange = true;
            startDate = DateUtil.getDate(from);
            endDate = DateUtil.getDate(to);
            endDate = addDays(endDate, 1);
        }
        if (dateRange && userId != null) {
            anyResponse = studentCourseFacade.findAll(startDate, endDate, userId);
        } else if (dateRange && userId == null) {
            anyResponse = studentCourseFacade.findAll(startDate, endDate);
        } else if (!dateRange && userId != null) {
            anyResponse = studentCourseFacade.findAll(userId);
        } else {
            anyResponse = studentCourseFacade.findAll();
        }
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("student")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudents(@QueryParam("from") String from, @QueryParam("to") String to) throws ParseException, JsonProcessingException {
        Date startDate, endDate;
        boolean dateRange = false;
        if (PredicateUtil.isSet(from) && PredicateUtil.isSet(to)) {
            dateRange = true;
        }
        if (dateRange) {
            startDate = DateUtil.getDate(from);
            endDate = DateUtil.getDate(to);
            endDate = addDays(endDate, 1);
            anyResponse = studentFacade.findAll(startDate, endDate);
        } else {
            anyResponse = studentFacade.findAll();
        }
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @GET
    @Path("exemption")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getExemptions(@QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("userId") Integer userId) throws ParseException, JsonProcessingException {
        Date startDate, endDate;
        boolean dateRange = false;
        if (PredicateUtil.isSet(from) && PredicateUtil.isSet(to)) {
            dateRange = true;
        }
        if (dateRange && userId != null) {
            startDate = DateUtil.getDate(from);
            endDate = DateUtil.getDate(to);
            endDate = addDays(endDate, 1);
            anyResponse = exemptionFacade.findSummary(startDate, endDate, userId);
        } else if (dateRange && userId == null) {
            startDate = DateUtil.getDate(from);
            endDate = DateUtil.getDate(to);
            endDate = addDays(endDate, 1);
            anyResponse = exemptionFacade.findSummary(startDate, endDate);
        } else if (!dateRange && userId != null) {
            anyResponse = exemptionFacade.findSummary(userId);
        } else {
            anyResponse = exemptionFacade.findAll();
        }
        httpStatus = Response.Status.OK;
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @POST
    @Path("declaration")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addDeclarations(Declaration entity) {
        declarationFacade.create(entity);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @PUT
    @Path("declaration")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDeclarations(Declaration entity) {
        anyResponse = declarationFacade.edit(entity);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }

    @DELETE
    @Path("declaration/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateDeclarations(@PathParam("id") Integer id) {
        Declaration declaration = declarationFacade.find(id);
        declarationFacade.remove(declaration);
        anyResponse = new CustomMessage(200, "Record removed successfully");
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

    @POST
    @Path("qualification/other")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addOtherCourseType(OtherCourse entity) {
        otherCourseFacade.create(entity);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @POST
    @Path("institution")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addInstitution(Institution entity) {
        institutionFacade.create(entity);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @GET
    @Path("audittrail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("from") String from, @QueryParam("to") String to, @QueryParam("userId") String userId) throws JsonProcessingException {
        Integer userId_=1;
        try {
            Date startDate, endDate;
            boolean dateRange = false;
            if (PredicateUtil.isSet(from) && PredicateUtil.isSet(to)) {
                dateRange = true;
            }
            if (dateRange && userId != null) {
                startDate = DateUtil.getDate(from);
                endDate = DateUtil.getDate(to);
                endDate = addDays(endDate, 1);
                anyResponse = auditTrailFacade.findAll(startDate, endDate, userId_);
            } else if (dateRange && userId == null) {
                startDate = DateUtil.getDate(from);
                endDate = DateUtil.getDate(to);
                endDate = addDays(endDate, 1);
                anyResponse = auditTrailFacade.findAll(startDate, endDate);
            } else if (!dateRange && userId != null) {
                anyResponse = auditTrailFacade.findAll(userId_);
            } else {
                anyResponse = auditTrailFacade.findAll();
            }
            httpStatus = Response.Status.OK;
        } catch (ParseException | CustomHttpException ex) {
            // Logger.getLogger(AdministratorRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        json = mapper.writeValueAsString(anyResponse);
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
}
