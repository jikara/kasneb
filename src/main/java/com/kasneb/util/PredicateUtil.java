/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

/**
 *
 * @author jikara
 */
public class PredicateUtil {

    public static Boolean isSet(String key) {
        return key != null && !key.trim().equals("");
    }
}
