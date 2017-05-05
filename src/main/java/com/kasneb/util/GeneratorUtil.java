/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import java.util.Random;

/**
 *
 * @author jikara
 */
public class GeneratorUtil {

    public static String generateReceiptNumber() {
        Integer maximum = 100000;
        Integer minimum = 1000;
        Random rn = new Random();
        int n = maximum - minimum + 1;
        int i = Math.abs(rn.nextInt() % n);
        return String.valueOf(minimum + i);
    }

    public static String generateRandomPassword() {
        Integer maximum = 10000000;
        Integer minimum = 1000000;
        Random rn = new Random();
        int n = maximum - minimum + 1;
        int i = Math.abs(rn.nextInt() % n);
        return String.valueOf(minimum + i);
    }

    public static String generateRandomPin() {
        Integer maximum = 10000;
        Integer minimum = 1000;
        Random rn = new Random();
        int n = maximum - minimum + 1;
        int i = Math.abs(rn.nextInt() % n);
        return String.valueOf(minimum + i);
    }

    public static String generateInvoiceNumber() {
        Integer maximum = 10000;
        Integer minimum = 1000;
        Random rn = new Random();
        int n = maximum - minimum + 1;
        int i = Math.abs(rn.nextInt() % n);
        return String.valueOf(minimum + i);
    }

    public static String generateRandomId() {
        Integer maximum = 100000000;
        Integer minimum = 100000;
        Random rn = new Random();
        int n = maximum - minimum + 1;
        int i = Math.abs(rn.nextInt() % n);
        return String.valueOf(minimum + i);
    }
}
