package com.ihp.frontoffice.helper;

import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimestampDbConverter {
    private static DateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm");
    //private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  /*  @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }*/


    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                //TimeZone timeZone = TimeZone.getTimeZone("IST");
                TimeZone timeZone = TimeZone.getDefault();
                df.setTimeZone(timeZone);
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }


    @TypeConverter
    public static String dateToTimestamp(Date value) {
        //TimeZone timeZone = TimeZone.getTimeZone("IST");
        TimeZone timeZone = TimeZone.getDefault();
        df.setTimeZone(timeZone);
        return value == null ? null : df.format(value);
    }
}
