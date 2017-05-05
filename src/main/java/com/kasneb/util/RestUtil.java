/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import com.google.gson.Gson;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.exception.CustomMessage;
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
        System.out.println(entity);
        ClientResponse response = webResource.type("application/json").post(ClientResponse.class, entity);
        if (response.getStatus() != 200) {
           // Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, response);
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "External error occured");
        }
        return response.getEntity(String.class);
    }

    public String doPut(String url, T entity) throws IOException, CustomHttpException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type("application/json").put(ClientResponse.class, entity);
        if (response.getStatus() != 200) {
           // Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, response);
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "External error occured");
        }
        return response.getEntity(String.class);
    }

    public String doGet(String url) throws IOException, CustomHttpException {
        Gson gson = new Gson();
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type("application/json").get(ClientResponse.class);
        switch (response.getStatus()) {
            case 200:
                return response.getEntity(String.class);
            case 500:
                String responseJson = response.getEntity(String.class);
                CustomMessage message = gson.fromJson(responseJson, CustomMessage.class);
               // Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, response);
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, message.getMessage());
            default:
               // Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, response);
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "External error occured");
        }
    }

    public String getHtml(String url) throws IOException, CustomHttpException {
        Client client = Client.create();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type("text/html").get(ClientResponse.class);
        if (response.getStatus() != 200) {
           // Logger.getLogger(RestUtil.class.getName()).log(Level.SEVERE, null, response);
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "External error occured");
        }
        return response.getEntity(String.class);
    }
}
