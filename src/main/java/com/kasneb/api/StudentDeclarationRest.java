/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.kasneb.util.PredicateUtil;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author jikara
 */
@Path("studentdeclaration")
@Stateless
public class StudentDeclarationRest {

    @Context
    private UriInfo context;
    ObjectMapper mapper = new ObjectMapper();
    Object anyResponse = new Object();
    Hibernate5Module hbm = new Hibernate5Module();
    Response.Status httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
    String json;
    @EJB
    com.kasneb.session.StudentDeclarationFacade studentDeclarationFacade;

    /**
     * Creates a new instance of StudentDeclarationRest
     */
    public StudentDeclarationRest() {
        hbm.enable(Hibernate5Module.Feature.REPLACE_PERSISTENT_COLLECTIONS);
        mapper.registerModule(hbm);
    }

    /**
     * Retrieves representation of an instance of
     * com.kasneb.api.StudentDeclarationRest
     *
     * @param id_
     * @param response_
     * @return an instance of javax.ws.rs.core.Response
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll(@QueryParam("id") String id_, @QueryParam("response") String response_) throws JsonProcessingException {
        if (PredicateUtil.isSet(id_) && PredicateUtil.isSet(response_)) {
            Integer id = Integer.parseInt(id_);
            Boolean response = Boolean.parseBoolean(response_);
            anyResponse = studentDeclarationFacade.findAll(id, response);
        } else if (PredicateUtil.isSet(id_) && !PredicateUtil.isSet(response_)) {
            Integer id = Integer.parseInt(id_);
            anyResponse = studentDeclarationFacade.findAll(id);
        } else if (!PredicateUtil.isSet(id_) && PredicateUtil.isSet(response_)) {
            Boolean response = Boolean.parseBoolean(response_);
            anyResponse = studentDeclarationFacade.findAll(response);
        } else {
            anyResponse = studentDeclarationFacade.findAll();
        }
        json = mapper.writeValueAsString(anyResponse);
        httpStatus = Response.Status.OK;
        return Response
                .status(httpStatus)
                .entity(json)
                .build();
    }
}
