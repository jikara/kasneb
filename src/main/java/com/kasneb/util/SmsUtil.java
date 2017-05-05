/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import com.google.gson.Gson;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.Sms;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author jikara
 */
public class SmsUtil {

    public static void sendSMS(Sms sms) throws UnsupportedEncodingException, IOException, CustomHttpException {
        Gson gson = new Gson();
        String url = "http://192.168.11.225/SMSService/API/SMS";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        // add header
        post.setHeader("DeveloperKey", "084049D0-AB72-4EE2-9EDE-0C25C1D1268C");
        post.setHeader("Password", "1d95a4c6d681ede5b18c89b21ceb46bfea7b8e4d8f824107615a2ee297493710");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("SenderName", "KASNEB"));
        urlParameters.add(new BasicNameValuePair("Mobile", sms.getPhoneNumber()));
        urlParameters.add(new BasicNameValuePair("Message", sms.getMessage()));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "An error occured");
        }
    }
}
