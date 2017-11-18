package com.hlibrary.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Xml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xmlpull.v1.XmlPullParser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlUtil {

    private XmlUtil() {
    }

    public static <T> List<T> pullParse(@NonNull String filename, @NonNull Class<T> cls) throws Exception {
        return pullParse(filename, cls, cls.getSimpleName());
    }

    public static <T> List<T> pullParse(@NonNull String filename, @NonNull Class<T> cls, @NonNull String tag) throws Exception {
        FileInputStream in = new FileInputStream(filename);
        return pullParse(in, cls, tag);
    }

    public static <T> List<T> pullParse(@NonNull InputStream in, @NonNull Class<T> cls) throws Exception {
        return pullParse(in, cls, cls.getSimpleName());
    }

    /**
     * @param in
     * @param cls
     * @param tag 实体结点名
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> pullParse(@NonNull InputStream in, @NonNull Class<T> cls, @NonNull String tag) throws Exception {
        List<T> models = null;
        JSONObject modelJson = null;
        XmlPullParser parser = Xml.newPullParser();// 得到Pull解析器
        parser.setInput(in, "UTF-8");// 设置下输入流的编码
        int eventType = parser.getEventType();// 得到第一个事件类型
        while (eventType != XmlPullParser.END_DOCUMENT) {// 如果事件类型不是文档结束的话则不断处理事件
            switch (eventType) {
                case (XmlPullParser.START_DOCUMENT):// 如果是文档开始事件
                    models = new ArrayList<>();// 创建一个person集合
                    break;
                case (XmlPullParser.START_TAG):// 如果遇到标签开始
                    String tagName = parser.getName();// 获得解析器当前元素的名称
                    if (tag.equals(tagName)) {// 如果当前标签名称是<person>
                        modelJson = new JSONObject();
                        collectAttr(modelJson, parser);
                    } else {
                        if (modelJson != null) {// 如果person已经创建完成
                            try {
                                final String value = parser.nextText();
                                if (!TextUtils.isEmpty(value))
                                    modelJson.put(tagName, value);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            collectAttr(modelJson, parser);
                        }
                    }
                    break;
                case (XmlPullParser.END_TAG):// 如果遇到标签结束
                    if (tag.equals(parser.getName())) {// 如果是person标签结束
                        models.add(JSON.parseObject(modelJson.toJSONString(), cls));// 将创建完成的person加入集合
                        modelJson = null;// 并且置空
                    }
                    break;
            }
            eventType = parser.next();// 进入下一个事件处理
        }
        return models;
    }

    private static void collectAttr(JSONObject modelJson, XmlPullParser parser) {
        final int count = parser.getAttributeCount();
        for (int i = 0; i < count; i++) {
            final String name = parser.getAttributeName(i);
            final String value = parser.getAttributeValue(i);
            modelJson.put(name, value);
        }
    }

}
