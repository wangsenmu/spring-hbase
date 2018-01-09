package com.wsm.spring.hbase.shared.util;

import com.google.common.base.Defaults;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.xml.datatype.XMLGregorianCalendar;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxu on 2017-01-13.
 */
public class ConvertUtil {
    
    private static String className = ConvertUtil.class.getSimpleName();
    
    /**
     * 如果s = null return ""， 否则返回s 本身。
     *
     * @param s String
     * @return String
     */
    public static String toString(String s) {
        return (s == null) ? "" : s;
    }
    
    /**
     * 返回Object类型的字符串形式;
     * 如果为null 则返回“”；否则返回object.toString();
     *
     * @param object Object
     */
    public static String toString(Object object) {
        return (null == object) ? "" : object.toString();
    }
    
    /**
     * 如果 s ＝null ，则返回null；
     * 否则按s.toString()字符串内容构造日期：
     * <br>格式为 “yyyy-mm-dd hh:mm:ss.fffffffff”，“yyyy-mm-dd hh:mm:ss”
     * 或“yyyy-mm-dd”有效。
     *
     * @param s String
     * @return Date
     */
    public static Date toDate(Object s) {
        return toDate(toString(s));
    }
    
    /**
     * 返回d
     *
     * @param d Date
     * @return Date
     */
    public static Date toDate(String d) {
        return DateUtil.stringToDate(d);
    }
    
    public static double toDouble(Object s) {
        return NumberUtils.toDouble(toString(s));
    }
    
    public static long toLong(Object s) {
        if (NumberUtils.isNumber(toString(s)))
            return new Double(toDouble(s)).longValue();
        else
            return NumberUtils.toLong(toString(s));
    }
    
    public static BigDecimal toBigDecimal(Object s) {
        return NumberUtils.createBigDecimal(toString(s));
    }
    
    /**
     * 如果s ＝ null 返回 0；否则返回s.toSting()对应的int
     *
     * @param s Object
     * @return int
     */
    public static int toInt(Object s) {
        if (NumberUtils.isNumber(toString(s)))
            return new Double(toDouble(s)).intValue();
        else
            return NumberUtils.toInt(toString(s));
    }
    
    /**
     * Object转换成short，如果转换失败返回0
     * 如果参数为null则返回0
     * <p>
     * <pre>
     *   toShort(null) = 0
     *   toShort("")   = 0
     *   toShort("1")  = 1
     * </pre>
     *
     * @param s 要转换的对象, 可null
     * @return short
     */
    public static short toShort(Object s) {
        if (NumberUtils.isNumber(toString(s)))
            return new Double(toDouble(s)).shortValue();
        else
            return NumberUtils.toShort(toString(s));
    }
    
    /**
     * return null;
     *
     * @return String
     */
    public static String getNull() {
        return null;
    }
    
    /**
     * dnyabean的结果对象
     *
     * @param object
     * @return 默认返回“”
     * @author XMJ
     */
    public static String getString(Object object) {
        String result = ""; // 默认为空
        if (object.toString().indexOf("java.lang.Object") == -1) {
            result = object.toString();
        }
        return result;
    }
    
    /**
     * dnyabean的结果对象,如果为空就输入指定默认为String def 描述： StrTool.java 时间： Nov 22, 2006
     *
     * @param object
     * @param def    指定默认String
     * @return 返回类型： String
     */
    public static String getString(Object object, String def) {
        String result = def; // 默认为def
        if (object.toString().indexOf("java.lang.Object") == -1) {
            result = object.toString();
        }
        return result;
    }
    
    /**
     * list转map
     */
    public static <K, V> Map<K, V> list2Map(List<V> list, String keyMethodName, Class<V> c) {
        Map<K, V> map = new HashMap<K, V>();
        if (list != null) {
            try {
                Method methodGetKey = c.getMethod(keyMethodName);
                for (V value : list) {
                    K key = (K) methodGetKey.invoke(value);
                    map.put(key, value);
                }
            } catch (Exception e) {
//                ClogManager.warn(className + "调用list2Map异常", e);
            }
        }
        return map;
    }
    
    /**
     * 泛型比较
     */
    public static <V> boolean compareEqual(V obj1, V obj2) {
        return compareEqual(obj1, obj2, false);
    }
    
    /**
     * 泛型对象比较
     *
     * @param obj1
     * @param obj2
     * @param ignoreCase 忽略大小写
     * @param <V>
     * @return
     */
    public static <V> boolean compareEqual(V obj1, V obj2, boolean ignoreCase) {
        //利用alibaba.fastjson
        String str1 = JSONUtil.toJson(obj1);
        String str2 = JSONUtil.toJson(obj2);
        
        if (ignoreCase)
            return StringUtils.equalsIgnoreCase(str1, str2);
        else
            return StringUtils.equals(str1, str2);
    }

    /**
     * 判断对错
     * @param s
     * @return
     */
    public static boolean toBoolean(Object s) {
        return Boolean.parseBoolean(toString(s));
    }

    /**
     * 通用对象处理
     *
     * @param obj
     * @param c
     * @param <V>
     * @return
     */
    public static <V> Object convertObject(final Object obj, Class<V> c) {
        
        if (obj == null)
            return Defaults.defaultValue(c);
        
        if (c.equals(String.class)) {
            return toString(obj);
        } else if (c.equals(long.class) || c.equals(Long.class)) {
            return toLong(obj);
        } else if (c.equals(int.class) || c.equals(Integer.class)) {
            return toInt(obj);
        } else if (c.equals(double.class) || c.equals(Double.class)) {
            return toDouble(obj);
        } else if (c.equals(short.class) || c.equals(Short.class)) {
            return toShort(obj);
        } else if (c.equals(char.class)) {
            return CharUtils.toChar(toString(obj));
        } else if (c.equals(byte.class)) {
            return NumberUtils.toByte(toString(obj));
        } else if (c.equals(float.class) || c.equals(Float.class)) {
            return NumberUtils.toFloat(toString(obj));
        } else if (c.equals(boolean.class) || c.equals(Boolean.class)) {
            return toBoolean(obj);
        } else if (c.equals(BigDecimal.class)) {
            return NumberUtils.createBigDecimal(toString(obj));
        } else if (c.equals(Date.class) || c.equals(java.sql.Date.class)) {
            return toDate(obj);
        } else if (c.equals(XMLGregorianCalendar.class)) {
//            return DateUtil.convertToXMLGregorianCalendar(toDate(obj));
        }
        return obj;
    }
}

