/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.session;

import com.google.gson.Gson;
import com.kasneb.entity.Country;
import com.kasneb.exception.CustomHttpException;
import com.kasneb.util.CoreUtil;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
            List<Country> countries = CoreUtil.getCountries();
            countries.stream().forEach((c) -> {
                em.merge(new Country(c.getCode(), c.getName(), c.getNationality(), c.getPhoneCode()));
            });
        } catch (IOException | CustomHttpException ex) {
            Logger.getLogger(CountryFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.findAll();
    }

}
