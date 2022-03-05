package com.hlibrary.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author linwh
 * @date 2015/10/28
 */
object DateFormatUtils {


    fun getDate(pattern: String): String {
        val format = SimpleDateFormat(pattern)
        return format.format(Date())
    }

    fun getDate(pattern: String, datetime: String, field: Int, value: Int): String? {
        val format = SimpleDateFormat(pattern)
        try {
            val date = format.parse(datetime)
            val c = Calendar.getInstance()
            c.time = date
            c.add(field, value)
            return format.format(c.time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun getCalendar(pattern: String, datetime: String): Calendar? {
        val format = SimpleDateFormat(pattern)
        try {
            val date = format.parse(datetime)
            val c = Calendar.getInstance()
            c.time = date
            return c
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return null
    }

    fun parse(pattern: String, c: Calendar): String {
        val format = SimpleDateFormat(pattern)
        return format.format(c.time)
    }

    fun parse(pattern: String, l: Long): String {
        val format = SimpleDateFormat(pattern)
        return format.format(l)
    }


}
