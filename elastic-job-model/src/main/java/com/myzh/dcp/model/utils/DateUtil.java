package com.myzh.dcp.model.utils;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

public class DateUtil extends DateUtils {
    public static Date now() {
        return new Date();
    }

    public static String getYearMonth() {
        return DateFormatUtils.format(new Date(), "yyyyMM");
    }

    public static Long unixTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    public static Long timestamp() {
        return System.currentTimeMillis();
    }

    public static boolean isSameHour(Date d1, Date d2) {
    	Calendar c1 = Calendar.getInstance();
    	c1.setTime(d1);
    	Calendar c2 = Calendar.getInstance();
    	c1.setTime(d2);
    	return c1.get(Calendar.HOUR_OF_DAY) == c2.get(Calendar.HOUR_OF_DAY);
    }
    
    public static boolean isSameDay(Date d1, Date d2) {
    	return DateUtils.isSameDay(d1, d2);
    }
    
    public static boolean isSameWeek(Date d1, Date d2) {
    	Calendar c1 = Calendar.getInstance();
    	c1.setTime(d1);
    	Calendar c2 = Calendar.getInstance();
    	c1.setTime(d2);
    	
    	return c1.getWeekYear() == c2.getWeekYear();
    }
    
    public static boolean isSameMonth(Date d1, Date d2) {
    	Calendar c1 = Calendar.getInstance();
    	c1.setTime(d1);
    	Calendar c2 = Calendar.getInstance();
    	c1.setTime(d2);
    	
    	return c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }
    public static boolean isSameYear(Date d1, Date d2) {
    	Calendar c1 = Calendar.getInstance();
    	c1.setTime(d1);
    	Calendar c2 = Calendar.getInstance();
    	c1.setTime(d2);
    	
    	return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
    }
    /**
     * 是否是1小时内
     *
     * @param date 时间
     * @return 1小时内=true
     */
    public static boolean inOneHour(Date date) {
        return System.currentTimeMillis() - date.getTime() <= 60 * 60 * 1000;
    }

    /**
     * 是否是24小时内
     *
     * @param date 时间
     * @return 24小时内=true
     */
    public static boolean inOneDay(Date date) {
        return System.currentTimeMillis() - date.getTime() <= 24 * 60 * 60 * 1000;
    }

    /**
     * 是否是3天内
     *
     * @param date 时间
     * @return 3天内=true
     */
    public static boolean inThreeDay(Date date) {
        return System.currentTimeMillis() - date.getTime() <= 3 * 24 * 60 * 60 * 1000;
    }

    /**
     * 是否是1周内
     *
     * @param date 时间
     * @return 1周内=true
     */
    public static boolean inOneWeek(Date date) {
        return System.currentTimeMillis() - date.getTime() <= 7 * 24 * 60 * 60 * 1000;
    }
    /**
     * 是否是1月内
     *
     * @param date 时间
     * @return 1周内=true
     */
    public static boolean inOneMonth(Date date) {
        return System.currentTimeMillis() - date.getTime() <= 30 * 7 * 24 * 60 * 60 * 1000;
    }
    /**
     * 是否是1月内
     *
     * @param date 时间
     * @return 1周内=true
     */
    public static boolean inOneYear(Date date) {
        return System.currentTimeMillis() - date.getTime() <= 365 * 30 * 7 * 24 * 60 * 60 * 1000;
    }

    /**
     * 相隔几天
     *
     * @param date 时间
     * @return 3天内=true
     */
    public static int apartDay(Date date) {
        return (int)(System.currentTimeMillis() - date.getTime() / (24 * 60 * 60 * 1000));
    }


    /**
     * 相隔几分钟
     * @param date
     * @return
     */
    public static Long apartMinute(Date date) {
        return Long.valueOf((System.currentTimeMillis() - date.getTime()) / (60 * 1000));
    }

    public static String getSimpleShowDateStr(Date date) {
        String dateStr = "";
        System.out.println(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss:sss"));
        long apartMinute = apartMinute(date);

        // 刚刚 3分钟内
        if(apartMinute <= 3) {
            dateStr = "刚刚";
        } else if(apartMinute <= 60) { // 1小时内
            dateStr = apartMinute + "分钟前";
        } else if(apartMinute <= 60 * 8) { // 8小时内
            dateStr = apartMinute / 60 + "小时前";
        } else if(apartMinute <= 60 * 24) {//一天内
            dateStr = "昨天";
        } else {
            dateStr = DateFormatUtils.format(date, "MM月dd日");
        }

        return dateStr;
    }

    public static Date parse(String dateStr, String... partten) {

        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        try {
            return DateUtils.parseDate(dateStr, partten);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
        
        try {
        	Date d1 = new Date();
			Thread.sleep(32000);
			Date d2 = new Date();
			
			System.out.println(DateUtil.isSameDay(d1, d2));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
        
    }
}
