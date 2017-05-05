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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jikara
 */
public class DateUtil {

    public static Date getDate(String dateString) throws ParseException {
        try {
            Date date;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            date = formatter.parse(dateString);
            return date;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new Date();
    }

    public static Date getDate(String dateString, String format) throws ParseException {
        Date date;
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        date = formatter.parse(dateString);
        return date;
    }

    public static Integer getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static String getString(Date date) throws ParseException {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            return formatter.format(date);
        }
        return "";
    }

    public static String getString(Date date, String format) throws ParseException {
        if (date != null) {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(date);
        }
        return "";
    }

    public static String convertFormat(String fromForamt, String toFormat, String dateString) {
        try {
            SimpleDateFormat fromFormatter = new SimpleDateFormat(fromForamt);
            Date date = fromFormatter.parse(dateString);
            SimpleDateFormat toFormatter = new SimpleDateFormat(toFormat);
            return toFormatter.format(date);
        } catch (ParseException ex) {
           // Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateString;
    }
}
