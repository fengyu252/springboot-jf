package com.zhouwei.springboot.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public  class  DateLocalTimeUtil {

    public static final String FORMAT_ALL = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_LONG = "yyyyMMddHHmmss";

    public static final String FORMAT_YEAR_MONTH = "yyyy-MM-dd";

    public static final String FORMAT_YEAR_MONTH2 = "yyyy-MM";

    public static final String FORMAT_YEARMONTH = "yyyyMMdd";

    /**
     *   joda的 date和java中的 date相互转换   使用的是UTC
     * @return
     */
    public static Date localDateTime2JavaDate(LocalDateTime localDateTime) {
        //获得到本地的时区
        ZoneId localZone = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(localZone);
        Date date = Date.from(zdt.toInstant());
        return date;
    }


    /**
     * 将 java中的Date转换为 joda time时间
     * @param date
     * @return
     */
    public static LocalDateTime javaDate2LocalDate(Date date) {
        //获得到本地的时区
        Instant instant = date.toInstant();
        ZoneId localZone = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(localZone).toLocalDateTime();
        return localDateTime;
    }


    /**
     * 将 joda localDatetime 转换为String类型
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String localDateTime2String(LocalDateTime localDateTime,String pattern) {
        DateTimeFormatter dtf = null;
        if (localDateTime == null) {
            throw new IllegalArgumentException("传入时间不能为空");
        }
        if (pattern == null) {
            dtf = DateTimeFormatter.ofPattern(FORMAT_ALL);
        }else {
            dtf = DateTimeFormatter.ofPattern(pattern);
        }

        return dtf.format(localDateTime);
    }

    /**
     * 将 java中的日期转为 String类型
     * @param date
     * @param pattern
     * @return
     */
    public static String javaDate2String(Date date,String pattern) {
        SimpleDateFormat sdf = null;
        if (date == null) {
            throw new IllegalArgumentException("传入时间不能为空");
        }
        if (pattern == null) {
            sdf = new SimpleDateFormat(FORMAT_ALL);
        }else {
            sdf = new SimpleDateFormat(pattern);
        }
        return sdf.format(date);
    }

    /**
     * 将字符串转为 java中的日期类型
     * @param date
     * @param pattern
     * @return
     */
    public static Date string2javaDate(String date,String pattern) {
        SimpleDateFormat sdf = null;
        if (date == null) {
            throw new IllegalArgumentException("传入时间不能为空");
        }
        if (pattern == null) {
            sdf = new SimpleDateFormat(FORMAT_ALL);
        }else {
            sdf = new SimpleDateFormat(pattern);
        }
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 String字符串转为 joda localDateTime
     * @param date
     * @param pattern
     * @return
     */
    public static LocalDateTime string2LocalDateTime(String date, String pattern) {
        DateTimeFormatter dtf = null;
        if (date == null) {
            throw new IllegalArgumentException("传入时间不能为空");
        }
        if (pattern == null) {
            dtf = DateTimeFormatter.ofPattern(FORMAT_ALL);
        }else {
            dtf = DateTimeFormatter.ofPattern(pattern);
        }

        return LocalDateTime.parse(date,dtf);
    }

}
