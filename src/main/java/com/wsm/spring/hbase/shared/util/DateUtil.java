package com.wsm.spring.hbase.shared.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangsm on 2016/10/27.
 * 日期帮助类，用于对日期以及字符串的相关操作
 */
public class DateUtil {

    private static String className = DateUtil.class.getSimpleName();

    public static final String DATE_FORMAT_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String DATEFORMATSECOND = "yyyyMMddHHmmss";
    public static final String DATEFORMATMILLIS = "yyyyMMddHHmmssSSS";
    public static final String DATE_FORMAT_ISO_DATETIME = "yyyy-MM-dd\'T\'HH:mm:ss";
    public static final String DATE_FORMAT_ISO_DATETIME_TIME_ZONE = "yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\'";
    public static final String DATE_FORMAT_SMTP = "EEE, dd MMM yyyy HH:mm:ss Z";


    /**
     * 获取日期时间中某一部分的日期时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date formatDateFormat(Date date, String format) {
        String str = dateToString(date, format);
        Date dateRe = stringToDate(str, format);
        return dateRe;
    }

    /**
     * compare two days
     * if first bigger than second return > 0 else return < 0
     */
    public static int compareDates(Date first, Date second) {
        if (first.getTime() > second.getTime()) {
            return 1;
        } else if (first.getTime() < second.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * @param first  第一个参数
     * @param second 第二个参数
     * @return return > 0 第一个大于第二个  反之亦之
     */
    @Deprecated
    public static int compareDateStr(String first, String second) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date firstDate = df.parse(first);
            Date secondDate = df.parse(second);
            return compareDates(firstDate, secondDate);
        } catch (Exception e) {
//            ClogManager.warn("DateUtil->getMaxDateStr方法", e);
        }
        return 0;
    }

    /**
     * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
     *
     * @param date       String 想要格式化的日期
     * @param oldPattern String 想要格式化的日期的现有格式
     * @param newPattern String 想要格式化成什么格式
     * @return String
     */
    @Deprecated
    public static String stringPattern(String date, String oldPattern, String newPattern) {
        if (date == null || oldPattern == null || newPattern == null)
            return "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern);        // 实例化模板对象
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);        // 实例化模板对象
        Date d = null;
        try {
            d = sdf1.parse(date);   // 将给定的字符串中的日期提取出来
        } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
//            ClogManager.warn("DateUtil -> stringPattern", e);
        }
        return sdf2.format(d);
    }

    /**
     * 获取两个日期中的所有日期列表
     *
     * @param fromDate
     * @param toDate
     * @return
     */
    public static List<Date> daysBetweenTwoDate(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return null;
        List<Date> result = new ArrayList<>();
        result.add(fromDate);
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(fromDate);
        tempStart.add(Calendar.DAY_OF_YEAR, 1);

        Calendar tempEnd = Calendar.getInstance();
        tempEnd.setTime(toDate);
        while (tempStart.before(tempEnd)) {
            result.add(tempStart.getTime());
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
        }
        result.add(toDate);
        return result;
    }

    /**
     * 获取天（一个月中的第几天）
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    public static void main(String[] args) {

        int day = getDay(new Date());
        System.out.println(day);

    }

    /**
     * 获取两个日期中的所有日java.lang.String期列表
     *
     * @param fromDate
     * @param toDate
     * @param dateFormat 日期格式
     * @return
     */
    public static List<String> daysBetweenTwoDate(String fromDate, String toDate, String dateFormat) {
        if (fromDate == null || "".equals(fromDate) || toDate == null || "".equals(toDate))
            return null;
        List<String> list = new LinkedList<>();
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        Date startDate = null;
        Date endDate = null;
        try {
            list.add(stringPattern(fromDate, "yyyy-MM-dd", "yyyy-MM-dd"));
            startDate = df.parse(fromDate);
            endDate = df.parse(toDate);
        } catch (ParseException e) {
//            ClogManager.warn("daysBetweenTwoDate方法异常", e);
        }
        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);
        while (true) {
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            if (startCalendar.getTimeInMillis() < endCalendar.getTimeInMillis()) {
                list.add(df.format(startCalendar.getTime()));
            } else {
                break;
            }
        }
        list.add(stringPattern(toDate, "yyyy-MM-dd", "yyyy-MM-dd"));
        return list;
    }

    /**
     * 判断当前日期是星期几<br>
     * <br>
     *
     * @param pTime 修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
    @Deprecated
    public static int dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
//            ClogManager.error("dayForWeek方法异常！", e);
        }
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 判断当前日期是星期几<br>
     * <br>
     *
     * @param pTime 修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
    public static int dayForWeek(Date pTime) {
        Calendar c = Calendar.getInstance();
        c.setTime(pTime);
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 功能描述：返回分
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取小时
     *
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取当前系统时间本月的第一天，以指定格式返回字符串
     *
     * @return
     */
    public static String getFirstDayOfThisMonth(Date date, String dateFormat) {
        String result = null;
        try {
            DateTime dateTime = new DateTime(date);
            DateTime dt = dateTime.withDayOfMonth(1);       //设置为1号,当前日期既为本月第一天
            return dateToString(dt.toDate(), dateFormat);

//            // yyyy-MM-dd
//            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
//            //获取前月的第一天
//            Calendar cal_1 = Calendar.getInstance();//获取当前日期
//            cal_1.setTime(date); //需要将date数据转移到Calender对象中操作
//            cal_1.add(Calendar.MONTH, 0);
//            cal_1.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
//            result = format.format(cal_1.getTime());
        } catch (Exception e) {
//            ClogManager.warn(className + "调用getFirstDayOfThisMonth异常", e);
        }
        return result;
    }

    /**
     * 获取一年以后当前系统时间的当前月的首日
     * yyyy-MM-dd
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String getOneYearLaterDay(Date date, String dateFormat) {
        return getFirstDayOfThisMonth(addDays(date, 365), dateFormat);
    }

    /**
     * 字符串转换成日期
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @param str
     * @return date
     */
    public static Date stringToDate(String str, String dateFormat) {
        Date date = null;
        try {
            if (StringUtils.isNotEmpty(str)) {
                DateTimeFormatter format = DateTimeFormat.forPattern(dateFormat);
                DateTime dateTime = DateTime.parse(str, format);
                date = dateTime.toDate();
            }
        } catch (Exception e) {
//            ClogManager.warn(className + "调用stringToDate异常", e);
        }
        return date;
    }


    public static Date stringToDate(String str) {
        Date date = null;
        try {
            if (StringUtils.isNotEmpty(str)) {
                DateTime dateTime = DateTime.parse(str);
                date = dateTime.toDate();
            }
        } catch (Exception e) {
//            ClogManager.warn(className + "调用stringToDate异常", e);
        }
        return date;
    }

    public static Calendar stringToCalendar(String str) {
        Calendar date = null;
        try {
            if (StringUtils.isNotEmpty(str)) {
                DateTime dateTime = DateTime.parse(str);
                date = dateTime.toCalendar(Locale.CHINA);
            }
        } catch (Exception e) {
//            ClogManager.warn(className + "调用stringToCalendar异常", e);
        }
        return date;
    }

    /**
     * 日期转换成字符串
     *
     * @param date
     * @param strFormat
     * @return
     */
    public static String dateToString(Date date, String strFormat) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(strFormat);
    }

    /**
     * 获取
     *
     * @param date
     * @param n
     * @return
     */
    public static Date addDays(Date date, int n) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(n).toDate(); //把日期往后增加n天.正数往后推,负数往前移动
    }

    /**
     * 增/减指定月数
     *
     * @param date
     * @param months
     * @return
     */
    public static Date addMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    public static Date addYears(Date date, int var1) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(var1).toDate(); //把日期往后增加n天.正数往后推,负数往前移动
    }

    /**
     * 计算相差分钟
     *
     * @param var0
     * @param var1
     * @return
     */
    public static int minutesBetween(Date var0, Date var1) {
        if (var0 != null && var1 != null)
            return Minutes.minutesBetween(new DateTime(var0), new DateTime(var1)).getMinutes();
        return 0;
    }

    /**
     * 计算相差天数
     *
     * @param var0
     * @param var1
     * @return
     */
    public static int daysBetween(Date var0, Date var1) {
        return Days.daysBetween(new DateTime(var0), new DateTime(var1)).getDays();
    }

    /**
     * 获取星期
     *
     * @param var1
     * @return
     */
    public static String getDayOfWeek(Date var1) {
        DateTime dateTime = new DateTime(var1);
        return dateTime.dayOfWeek().getAsString();
    }

    /*
     * 生成日期对象
     */
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar gc = null;
        try {
            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (Exception e) {
        }
        return gc;
    }

    public static Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}
