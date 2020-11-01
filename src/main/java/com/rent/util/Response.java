package com.rent.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Response extends ResponseBase {
    public static void error() {
        error(500, "未知异常，请联系管理员");
    }

    public static void error(String msg) {
        error(500, msg);
    }

    public static void error(int code, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", Integer.valueOf(code));
        map.put("msg", msg);
        json(map);
    }

    public static void ok() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", Integer.valueOf(0));
        map.put("msg", "success");
        json(map);
    }

    public static void string(String message) {
        (new Response()).setContentType("text/html;charset=UTF-8").write(message);
    }

    public static void json(String key, Object val) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, val);
        json(map);
    }

    public static void redirect(String url) {
        (new Response()).sendRedirect(url);
    }

    public static void json(Object obj) {
        byte[] bytes = JSONObject.toJSONString(obj, new SerializerFeature[]{SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteDateUseDateFormat}).getBytes();
        (new Response()).setContentType("application/json;charset=utf-8").write(bytes);
    }

    public static void download(String filename, InputStream is) {
        try {
            String fileName = new String(filename.getBytes("UTF-8"), "iso-8859-1");
            (new Response()).setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", new Object[]{fileName})).write(is);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void download(String filename, File file) {
        try {
            download(filename, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void download(String filename, byte[] bytes) {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        download(filename, bais);
    }

    public static void result(int code, String msg, Object val) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", Integer.valueOf(code));
        map.put("msg", msg);
        map.put("val", val);
        json(map);
    }
}
