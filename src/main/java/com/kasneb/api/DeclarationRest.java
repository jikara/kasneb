/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.kasneb.entity.StudentDeclaration;
import com.kasneb.exception.CustomMessage;
import com.kasneb.exception.CustomHttpException;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("declaration")
public class DeclarationRest {

    Object anyResponse = new Object();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    @EJB
    com.kasneb.session.DeclarationFacade declarationFacade;

    /**
     * Creates a new instance of StudentRest
     */
    public DeclarationRest() {
    }

    /**
     * Retrieves representation of an instance of com.kasneb.api.StudentRest
     *
     * @return an instance of javax.ws.rs.core.Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        anyResponse = declarationFacade.findAll();

        return Response
                .status(Response.Status.OK)
                .entity(anyResponse)
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
        anyResponse = declarationFacade.find(id);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

    /**
     *
     * @param entity
     * @return
     */
    @PUT
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(StudentDeclaration entity) {
        try {
            declarationFacade.update(entity);
            anyResponse = "Declaration  updated successfully";
            httpStatus = Response.Status.OK;
        } catch (CustomHttpException ex) {
            anyResponse = new CustomMessage(ex.getStatusCode().getStatusCode(), ex.getMessage());
            httpStatus = ex.getStatusCode();
        }
        return Response
                .status(httpStatus)
                .entity(anyResponse)
                .build();
    }

}
