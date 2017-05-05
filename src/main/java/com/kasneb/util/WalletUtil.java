/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.kasneb.entity.Invoice;
import com.kasneb.entity.Payment;
import com.kasneb.entity.Student;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.model.CompletePaymentResponse;
import com.kasneb.model.ErrorResponse;
import com.kasneb.model.LoginResponse;
import com.kasneb.model.PreparePaymentResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author jikara
 */
public class WalletUtil {

    public static boolean walletExists(String phoneNumber) throws IOException, CustomHttpException {
        LoginResponse loginResponse = getAgentToken(Constants.AGENT_EMAIL, Constants.AGENT_PASSWORD);
        String url = Constants.CORPORATE_URL + "api/Payments/GetWalletExists?Stream=wallet&PhoneNumber=" + phoneNumber;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        // add header
        get.setHeader("Authorization", "bearer " + loginResponse.getAccess_token());
        get.setHeader("app_key", Constants.APP_KEY);
        HttpResponse response = client.execute(get);
        return response.getStatusLine().getStatusCode() == 200;
    }

    public static void walletStatement(String phoneNumber) throws IOException, CustomHttpException {
        LoginResponse loginResponse = getWalletToken(Constants.AGENT_EMAIL, Constants.AGENT_PASSWORD);
        String url = Constants.CORPORATE_URL + "api/Payments/GetWalletExists?Stream=wallet&PhoneNumber=" + phoneNumber;
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url);
        // add header
        get.setHeader("Authorization", "bearer " + loginResponse.getAccess_token());
        get.setHeader("app_key", Constants.APP_KEY);
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "An error occured.Your request could not be processed.");
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
    }

    public static void createWallet(Student student, String pin) throws CustomHttpException, IOException {
        String idNumber = GeneratorUtil.generateRandomId();
        if (student.getDocumentNo() != null) {
            idNumber = student.getDocumentNo();
        }
        LoginResponse loginResponse = getAgentToken(Constants.AGENT_EMAIL, Constants.AGENT_PASSWORD);
        ObjectMapper mapper = new ObjectMapper();
        PreparePaymentResponse preparePaymentResponse;
        String line;
        String url = Constants.CORPORATE_URL + "api/Payments/postwalletregister";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        // add header
        post.setHeader("Authorization", "bearer " + loginResponse.getAccess_token());
        post.setHeader("app_key", Constants.APP_KEY);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("Stream", "wallet"));
        urlParameters.add(new BasicNameValuePair("PhoneNumber", student.getPhoneNumber()));
        urlParameters.add(new BasicNameValuePair("IDNumber", idNumber));
        urlParameters.add(new BasicNameValuePair("FirstName", student.getFirstName()));
        urlParameters.add(new BasicNameValuePair("MiddleName", student.getMiddleName()));
        urlParameters.add(new BasicNameValuePair("LastName", student.getLastName()));
        urlParameters.add(new BasicNameValuePair("Pin", pin));
        urlParameters.add(new BasicNameValuePair("Email", student.getEmail()));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, "An error occured.Your request could not be processed.");
        }
    }

    public static LoginResponse getAgentToken(String username, String password) throws CustomHttpException, UnsupportedEncodingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        LoginResponse corporateLoginResponse;
        String line;
        String url = Constants.CORPORATE_URL + "Token";
        HttpClient client = new DefaultHttpClient();
        HttpPost put = new HttpPost(url);
        // add header
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "agency"));
        urlParameters.add(new BasicNameValuePair("username", username));
        urlParameters.add(new BasicNameValuePair("password", password));
        put.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(put);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, response.getStatusLine().getReasonPhrase());
        }
        corporateLoginResponse = mapper.readValue(result.toString(), LoginResponse.class);
        return corporateLoginResponse;
    }

    public static LoginResponse getWalletToken(String username, String password) throws CustomHttpException, UnsupportedEncodingException, IOException, SocketException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        LoginResponse corporateLoginResponse;
        ErrorResponse errorResponse;
        String line;
        String url = Constants.CORPORATE_URL + "Token";
        HttpClient client = new DefaultHttpClient();
        HttpPost put = new HttpPost(url);
        // add header
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("grant_type", "wallet"));
        urlParameters.add(new BasicNameValuePair("username", username));
        urlParameters.add(new BasicNameValuePair("password", password));
        put.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(put);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            errorResponse = mapper.readValue(result.toString(), ErrorResponse.class);
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, errorResponse.getError_description());
        }
        corporateLoginResponse = mapper.readValue(result.toString(), LoginResponse.class);
        return corporateLoginResponse;
    }

    public static PreparePaymentResponse preparePayment(LoginResponse loginResponse, Payment entity, Invoice invoice) throws CustomHttpException, UnsupportedEncodingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = null;
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        PreparePaymentResponse preparePaymentResponse;
        String line;
        String url = Constants.CORPORATE_URL + "api/Payments/Post";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        // add header
        post.setHeader("Authorization", "bearer " + loginResponse.getAccess_token());
        post.setHeader("app_key", Constants.APP_KEY);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("PhoneNumber", entity.getPhoneNumber()));
        urlParameters.add(new BasicNameValuePair("PaidBy", invoice.getStudentCourse().getStudent().getFirstName()));
        urlParameters.add(new BasicNameValuePair("PaymentTypeID", "1"));
        urlParameters.add(new BasicNameValuePair("Stream", "merchantpayment"));
        urlParameters.add(new BasicNameValuePair("Amount", String.valueOf(entity.getAmount())));
        urlParameters.add(new BasicNameValuePair("MerchantCode", Constants.MERCHANT_CODE));
        urlParameters.add(new BasicNameValuePair("ProductName", invoice.getFeeCode().getCode()));
        urlParameters.add(new BasicNameValuePair("InvoiceNumber", invoice.getReference()));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            errorResponse = mapper.readValue(result.toString(), ErrorResponse.class);
            //throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, errorResponse.getMessage());
        }
        preparePaymentResponse = mapper.readValue(result.toString(), PreparePaymentResponse.class);
        return preparePaymentResponse;
    }

    public static CompletePaymentResponse completePayment(LoginResponse loginResponse, PreparePaymentResponse preparePaymentResponse, String phoneNumber, String pin) throws UnsupportedEncodingException, IOException, CustomHttpException {
        ObjectMapper mapper = new ObjectMapper();
        ErrorResponse errorResponse = null;
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibilityChecker(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        CompletePaymentResponse completePaymentResponse;
        String line;
        String url = Constants.CORPORATE_URL + "api/Payments/Put";
        HttpClient client = new DefaultHttpClient();
        HttpPut put = new HttpPut(url);
        // add header
        put.setHeader("Authorization", "bearer " + loginResponse.getAccess_token());
        put.setHeader("app_key", Constants.APP_KEY);

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("Stream", "merchantpayment"));
        urlParameters.add(new BasicNameValuePair("PhoneNumber", phoneNumber));
        urlParameters.add(new BasicNameValuePair("TransactionID", preparePaymentResponse.getTransactionID()));
        urlParameters.add(new BasicNameValuePair("Pin", pin));
        put.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(put);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuilder result = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        if (response.getStatusLine().getStatusCode() != 200) {
            errorResponse = mapper.readValue(result.toString(), ErrorResponse.class);
            throw new CustomHttpException(Response.Status.INTERNAL_SERVER_ERROR, errorResponse.getMessage());
        }
        completePaymentResponse = mapper.readValue(result.toString(), CompletePaymentResponse.class);
        return completePaymentResponse;
    }

}
