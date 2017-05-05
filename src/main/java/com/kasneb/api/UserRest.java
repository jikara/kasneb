/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.kasneb.entity.Login;
import com.kasneb.entity.User;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
import com.kasneb.util.GeneratorUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("user")
public class UserRest {

    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    @EJB
    com.kasneb.session.UserFacade userFacade;

    /**
     * Creates a new instance of UserRest
     */
    public UserRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.UserRest
     *
     * @return an instance of Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<User> users = userFacade.findAll();
        return Response
                .status(httpStatus)
                .entity(users)
                .build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Integer id) {
        User user = userFacade.find(id);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(user)
                .build();
    }

    @GET
    @Path("verify")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findVerifyUsers() {
        List<User> users = userFacade.findVerifyUsers();
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(users)
                .build();
    }

    /**
     * PUT method for updating or creating an instance of UserRest
     *
     * @param entity
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(User entity)   {
        entity = userFacade.update(entity);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @PUT
    @Path("changepassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword( User entity)  {
        try {
            userFacade.changePassword(entity);
            anyResponse = new CustomMessage(200, "Password saved successfully");
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
            // Logger.getLogger(UserRest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            anyResponse = new CustomMessage(500, ex.getMessage());
            // Logger.getLogger(UserRest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create( User entity )  {
        Login login = new Login(entity.getEmail(), GeneratorUtil.generateRandomPassword());
        login.setEmailActivated(true);
        entity.setLoginId(login);
        entity.setCreated(new Date());
        userFacade.create(entity);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(entity)
                .build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Integer id) {
        try {
            User user = userFacade.find(id);
            userFacade.remove(user);
            httpStatus = Response.Status.OK;
        } catch (Exception ex) {
        }
        return Response
                .status(httpStatus)
                .entity(null)
                .build();
    }

}
