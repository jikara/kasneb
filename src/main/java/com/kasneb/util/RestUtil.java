/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import com.kasneb.exception.CustomHttpException;
import java.io.IOException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.Response;

/**
 *
 * @author jikara
 * @param <T>
 */
public class RestUtil<T> {

    public String doPost(String url, T entity) throws IOException, CustomHttpException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, entity);
        if (response.getStatus() != 200) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "External error occured");
        }
        return response.getEntity(String.class);
    }

    public String doPut(String url, T entity) throws IOException, CustomHttpException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type("application/json").put(ClientResponse.class, entity);
        if (response.getStatus() != 200) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "External error occured");
        }
        return response.getEntity(String.class);
    }

    public String doGet(String url) throws IOException, CustomHttpException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
        if (response.getStatus() != 200) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "External error occured");
        }
        return response.getEntity(String.class);
    }
}
