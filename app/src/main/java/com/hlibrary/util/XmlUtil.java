package com.hlibrary.util;

import android.text.TextUtils;
import android.util.Xml;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xmlpull.v1.XmlPullParser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linwenhui
 * @date 20150101
 */
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
        // 得到Pull解析器
        XmlPullParser parser = Xml.newPullParser();
        // 设置下输入流的编码
        parser.setInput(in, "UTF-8");
        // 得到第一个事件类型
        int eventType = parser.getEventType();
        // 如果事件类型不是文档结束的话则不断处理事件
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // 如果是文档开始事件
                case (XmlPullParser.START_DOCUMENT):
                    // 创建一个person集合
                    models = new ArrayList<>();
                    break;
                // 如果遇到标签开始
                case (XmlPullParser.START_TAG):
                    // 获得解析器当前元素的名称
                    String tagName = parser.getName();
                    // 如果当前标签名称是<person>
                    if (tag.equals(tagName)) {
                        modelJson = new JSONObject();
                        collectAttr(modelJson, parser);
                    } else {
                        // 如果person已经创建完成
                        if (modelJson != null) {
                            try {
                                final String value = parser.nextText();
                                if (!TextUtils.isEmpty(value)) {
                                    modelJson.put(tagName, value);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            collectAttr(modelJson, parser);
                        }
                    }
                    break;
                // 如果遇到标签结束
                case (XmlPullParser.END_TAG):
                    // 如果是person标签结束
                    if (tag.equals(parser.getName())) {
                        // 将创建完成的person加入集合
                        models.add(JSON.parseObject(modelJson.toJSONString(), cls));
                        // 并且置空
                        modelJson = null;
                    }
                    break;
                default:
                    break;
            }
            // 进入下一个事件处理
            eventType = parser.next();
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
