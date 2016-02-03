package com.myzh.dpc.console.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();


    public static Map json2Map(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T json2Bean(String json, Class<T> obj) {
        try {
            return objectMapper.readValue(json, obj);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String bean2Json(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static List<Map> json2List(String json) {
        try {
            return objectMapper.readValue(json, List.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String list2Json(Object jsonList) {
        try {
            return objectMapper.writeValueAsString(jsonList);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
