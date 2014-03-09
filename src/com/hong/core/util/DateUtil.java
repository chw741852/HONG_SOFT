package com.hong.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hong on 14-1-18 下午10:02.
 */
public class DateUtil {
    /**
     * 获取当前日期 yyyy-MM-dd
     * @return 日期
     */
    public static String getDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(new Date());
    }

    /**
     * 获取当前时间 HH:mm:ss
     * @return
     */
    public static String getTime() {
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        return fmt.format(new Date());
    }

    /**
     * 获取日期时间 yyyy-MM-dd HH:mm:ss
     * @return 日期时间
     */
    public static String getDateTime() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(new Date());
    }

    /**
     * 指定日期格式
     * @param format 日期格式
     * @return  日期
     */
    public static String getDate(String format) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(new Date());
    }

    /**
     * 指定日期，指定格式
     * @param format    格式
     * @param date  日期
     * @return  日期
     */
    public static String formatDate(String format, Date date) {
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }
}
