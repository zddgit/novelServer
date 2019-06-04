package com.free.novel.util;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

public class JsonUtil {

    public static String obj2Json(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSON.toJSONString(obj);
    }

    public static <T> T json2Object(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static <T> List<T> json2ArrayListObject(String json, Class<T> clazz) {
        try {
            return JSON.parseArray(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Map object2Map(Object obj, Class<? extends Map> clazz) {
        if (obj instanceof String) {
            return JSON.parseObject((String) obj, clazz);
        }
        return JSON.parseObject(JSON.toJSONString(obj), clazz);
    }

}
