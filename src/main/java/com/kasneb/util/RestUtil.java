/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import com.kasneb.exception.CustomHttpException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.glassfish.jersey.client.ClientResponse;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.Response;

/**
 *
 * @author jikara
 */
public class RestUtil {

    public static Object doPost(String url, String json) throws IOException {
        try {
            Client client = Client.create();
            WebResource webResource = client.resource(url);
            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, json);
            if (response.getStatus() != 200) {
                throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "External error occured");
            }
            String output = response.readEntity(String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Map to 
        return null;
    }

    public static String doGet(String url) throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        // add request header
        request.addHeader("User-Agent", "Mozilla/5.0");
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        String json = result.toString();
        return json;
    }
}
