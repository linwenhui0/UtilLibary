package com.hlibrary.util

import android.text.TextUtils
import android.util.Xml
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.xmlpull.v1.XmlPullParser
import java.io.FileInputStream
import java.io.InputStream
import java.util.*

/**
 * @author linwenhui
 * @date 20150101
 */
object XmlUtil {

    @Throws(Exception::class)
    fun <T> pullParse(filename: String, cls: Class<T>): List<T>? {
        return pullParse(filename, cls, cls.simpleName)
    }

    @Throws(Exception::class)
    fun <T> pullParse(filename: String, cls: Class<T>, tag: String): List<T>? {
        val `in` = FileInputStream(filename)
        return pullParse(`in`, cls, tag)
    }

    @Throws(Exception::class)
    fun <T> pullParse(`in`: InputStream, cls: Class<T>): List<T>? {
        return pullParse(`in`, cls, cls.simpleName)
    }

    /**
     * @param in
     * @param cls
     * @param tag 实体结点名
     * @param <T>
     * @return
     * @throws Exception
    </T> */
    @Throws(Exception::class)
    fun <T> pullParse(`in`: InputStream, cls: Class<T>, tag: String): List<T>? {
        var models: MutableList<T>? = null
        var modelJson: JSONObject? = null
        // 得到Pull解析器
        val parser = Xml.newPullParser()
        // 设置下输入流的编码
        parser.setInput(`in`, "UTF-8")
        // 得到第一个事件类型
        var eventType = parser.eventType
        // 如果事件类型不是文档结束的话则不断处理事件
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                // 如果是文档开始事件
                XmlPullParser.START_DOCUMENT ->
                    // 创建一个person集合
                    models = ArrayList<T>()
                // 如果遇到标签开始
                XmlPullParser.START_TAG -> {
                    // 获得解析器当前元素的名称
                    val tagName = parser.name
                    // 如果当前标签名称是<person>
                    if (tag == tagName) {
                        modelJson = JSONObject()
                        collectAttr(modelJson, parser)
                    } else {
                        // 如果person已经创建完成
                        if (modelJson != null) {
                            try {
                                val value = parser.nextText()
                                if (!TextUtils.isEmpty(value)) {
                                    modelJson[tagName] = value
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            collectAttr(modelJson, parser)
                        }
                    }
                }
                // 如果遇到标签结束
                XmlPullParser.END_TAG ->
                    // 如果是person标签结束
                    if (tag == parser.name) {
                        // 将创建完成的person加入集合
                        models!!.add(JSON.parseObject(modelJson!!.toJSONString(), cls))
                        // 并且置空
                        modelJson = null
                    }
                else -> {
                }
            }
            // 进入下一个事件处理
            eventType = parser.next()
        }
        return models
    }

    private fun collectAttr(modelJson: JSONObject, parser: XmlPullParser) {
        val count = parser.attributeCount
        for (i in 0 until count) {
            val name = parser.getAttributeName(i)
            val value = parser.getAttributeValue(i)
            modelJson[name] = value
        }
    }

}
