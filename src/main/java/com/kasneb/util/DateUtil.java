/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kasneb.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author jikara
 */
public class DateUtil {

    public static Date getDate(String stringDate) throws ParseException {
        Date date;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        date = formatter.parse(stringDate);
        return date;
    }

    public static String getString(Date date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static Date getToDate(String stringDate) throws ParseException {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = formatter.parse(stringDate);
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }
}
