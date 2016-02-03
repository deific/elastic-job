package com.myzh.dpc.console.utils;

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
        Date date = new Date();
        System.out.println(DateUtil.getSimpleShowDateStr(date));
    }
}
