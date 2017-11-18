package com.hlibrary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by linwenhui on 2015/10/28.
 */
public class DateFormatUtil {
    private DateFormatUtil() {
    }

    public static String getDate(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date());
    }

    public static String getDate(String pattern, String datetime, int field, int value) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date date = format.parse(datetime);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(field, value);
            return format.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Calendar getCanlendar(String pattern, String datetime) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            Date date = format.parse(datetime);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            return c;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parse(String pattern, Calendar c) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(c.getTime());
    }

    public static String parse(String pattern, long l) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(l);
    }

}
