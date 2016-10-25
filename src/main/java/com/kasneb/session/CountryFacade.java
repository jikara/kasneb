/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kasneb.entity.Country;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author jikara
 */
@Stateless
public class CountryFacade extends AbstractFacade<Country> {

    @PersistenceContext(unitName = "com.kasneb_kasneb_new_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    Gson gson = new Gson();

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CountryFacade() {
        super(Country.class);
    }

    @Override
    public List<Country> findAll() {
        try {
            List<Country> countries = gson.fromJson(getJson(), new TypeToken<List<Country>>() {
            }.getType());
            for (Country c : countries) {
                em.merge(new Country(c.getCode(), c.getName(), c.getNationality(), c.getPhoneCode()));
            }
        } catch (IOException ex) {
            Logger.getLogger(CountryFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.findAll();
    }

    public String getJson() throws IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://localhost:29097/core/api/nation");
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
