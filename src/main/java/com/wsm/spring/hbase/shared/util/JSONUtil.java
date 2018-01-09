package com.wsm.spring.hbase.shared.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.google.common.base.Defaults;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by wangsm on 2016/11/3.
 */
public class JSONUtil {

    private static String className = JSONUtil.class.getSimpleName();

    public static boolean isJsonObject(String strJson) {
        if (StringUtils.isBlank(strJson))
            return false;
        strJson = strJson.trim();
        if (strJson.startsWith("{") && strJson.endsWith("}")) {
            return true;
        }
        return false;
    }

    public static boolean isJsonArray(String strJson) {
        if (StringUtils.isBlank(strJson))
            return false;
        strJson = strJson.trim();
        if (strJson.startsWith("[") && strJson.endsWith("]")) {
            return true;
        }
        return false;
    }

    public static String toJson(Object o) {
        return toJson(o, SerializerFeature.UseISO8601DateFormat,SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     * JSON数据转化为string
     *
     * @param o
     * @param features json转化要求，参考 SerializerFeature
     * @return
     */
    public static String toJson(Object o, SerializerFeature... features) {
        return toJson(o, null, features);
    }

    public static String toJson(Object o, String PropertyFilter) {
        return toJson(o, PropertyFilter, SerializerFeature.UseISO8601DateFormat, SerializerFeature.DisableCircularReferenceDetect);
    }

    /**
     *
     * @param o
     * @param filter 区分大小写
     * @param features
     * @return
     */
    public static String toJson(Object o, final String filter, SerializerFeature... features) {
        if (o == null)
            return null;

        SimplePropertyPreFilter propertyFilter = null;
        if (StringUtils.isNotEmpty(filter)){
            propertyFilter = new SimplePropertyPreFilter();
            propertyFilter.getExcludes().add(filter);
        }

        String retValue = null;
        try {
            if (propertyFilter != null)
                retValue = JSON.toJSONString(o,propertyFilter, features);
            else
                retValue = JSON.toJSONString(o, features);
        } catch (Exception e) {
//            ClogManager.error(className + "调用toJson异常",
//                    String.format("参数：%s，错误信息：%s", o.getClass(), e.getMessage()));
        }
        return retValue;
    }

    public static <T> T fromJson(String json, Class<T> ca) {
        return fromJson(json, ca, new Feature[0]);
    }

    /**
     * json数据字符串 转化为对象
     *
     * @param json
     * @param ca
     * @param features json转化要求，参考 Feature
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> ca, Feature... features) {
        T t = null;
        try {
            if (StringUtils.isBlank(json))
                return t;
            if (isJsonObject(json)) {
                t = JSON.parseObject(json, ca, features);
            }
        } catch (Exception e) {
//            ClogManager.error(className + "调用fromJson异常", "参数类型：" + ca.getName() + "。错误信息：" + e.getMessage());
        }
        return t;
    }

    /**
     * Json数据转化list对象，和fromJson区别主要在于无法精细操作（缺少features）
     *
     * @param json
     * @param ca
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> ca) {
        List<T> t = null;
        try {
            if (StringUtils.isBlank(json))
                return t;
            if (isJsonArray(json))
                t = JSON.parseArray(json, ca);
        } catch (Exception e) {
//            ClogManager.error(className + "调用fromJsonToList异常", "参数类型：" + ca.getName() + "。错误信息：" + e.getMessage());
        }
        return t;
    }

    /**
     * Json数据转化Map对象，类似于fromJson
     *
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> formatJsonToMap(String jsonStr) {
        JSONObject jsonObject = null;
        if (StringUtils.isBlank(jsonStr))
            return jsonObject;
        try {
            if (isJsonObject(jsonStr))
                jsonObject = JSON.parseObject(jsonStr);
            if (jsonObject != null && !jsonObject.isEmpty())
                return jsonObject;
        } catch (Exception e) {
//            ClogManager.error(className + "调用formatJsonToMap异常", e);
        }
        return jsonObject;
    }

    /**
     * 获取对象的value值
     *
     * @param jsonStr JsonObject 或者 Map对象
     * @param key
     * @param c       一般基本类型的class
     * @param <T>     支持一般类型
     * @return 一般类型 不支持list
     */
    public static <T> T readKey(String jsonStr, String key, Class<T> c) {
        T t = null;
        if (StringUtils.isBlank(jsonStr) || StringUtils.isBlank(key))
            return t;
        if (!isJsonObject(jsonStr))
            return t;
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        if (jsonObject != null && !jsonObject.isEmpty())
            t = readKey(jsonObject, key, c);
        return t;
    }

    /**
     * 获取对象的value值
     *
     * @param obj JsonObject 或者 Map对象
     * @param key
     * @param c   一般基本类型的class
     * @param <T> 支持一般类型
     * @return 一般类型 不支持list
     */
    public static <T> T readKey(Object obj, String key, Class<T> c) {
        Object rest;
        try {
            if (obj != null && isJsonObject(ConvertUtil.toString(obj))) {
                JSONObject jsonObject = (JSONObject) obj;
                Object result = jsonObject.get(key);
                rest = ConvertUtil.convertObject(result, c);
            } else
                rest = Defaults.defaultValue(c);
            return (T) rest;
        } catch (Exception e) {
//            ClogManager.error(className + "调用readKey异常", e);
        }
        return null;
    }

    /**
     * @param json json对象
     * @param path 查询对象名称节点
     * @param c    一般基本类型的class
     * @param <T>  支持一般类型
     * @return 一般类型 不支持list
     */
    public static <T> T readPath(String json, String path, Class<T> c) {
        if (StringUtils.isBlank(json))
            return null;
        Object result = JSON.parse(json);
        return readPath(result, path, c);
    }

    /**
     * @param object JsonObject 或者 Map对象
     * @param path
     * @param c      一般基本类型的class
     * @param <T>    支持一般类型
     * @return 一般类型 不支持list
     */
    public static <T> T readPath(Object object, String path, Class<T> c) {
        Object result = null;
        try {
            if (object == null || StringUtils.isEmpty(path) || c == null)
                return c != null ? Defaults.defaultValue(c) : null;
            JSONPath jsonPath = JSONPath.compile(path);
            Object obj = jsonPath.eval(object);
            result = ConvertUtil.convertObject(obj, c);
        } catch (Exception ex) {
//            ClogManager.warn(className + "调用readPath异常", ex);
        }
        return result != null ? (T) result : null;
    }

    /**
     * 读取JSONPath list对象
     *
     * @param json JsonObject 或者 Map对象
     * @param path 查询对象名称节点
     * @return List<JsonObject>
     */
    public static List readListPath(String json, String path) {
        Object result = JSON.parse(json);
        return readListPath(result, path);
    }

    /**
     * 读取JSONPath list对象
     *
     * @param object JsonObject 或者 Map对象
     * @param path   查询对象名称节点
     * @return List<JsonObject>
     */
    public static List readListPath(Object object, String path) {
        List<Object> listRes = null;
        try {
            if (object == null) return listRes;
            int size = JSONPath.size(object, path);
            if (size > 0) {
                JSONPath jsonPath = JSONPath.compile(path);
                Object obj = jsonPath.eval(object);

                listRes = (List) obj;
            }
        } catch (Exception ex) {
//            ClogManager.warn(className + "调用readListPath异常", ex);
        }
        return listRes;
    }

    @Deprecated
    public static JSONObject parseObject(String jsonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(jsonString);
        } catch (Exception e) {
        }
        return jsonObject;
    }
}
