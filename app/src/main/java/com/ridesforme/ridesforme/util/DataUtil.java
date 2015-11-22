package com.ridesforme.ridesforme.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Robson on 11/11/2015.
 */
public class DataUtil {

    public static Date stringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String reportDate="";
        try {
             reportDate = df.format(date);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return reportDate;
    }
}
