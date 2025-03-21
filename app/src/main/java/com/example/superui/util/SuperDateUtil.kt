package com.example.superui.util

import android.icu.util.Calendar
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

object SuperDateUtil {
    fun currentYear() : Int{
        /**
         * 当前年
         */
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    /**
     * 将毫秒格式化为分:秒，例如：150:11
     *
     * @param data
     * @return
     */
    fun ms2ms(data: Int): String {
        var data = data
        if (data == 0) {
            return "00:00"
        }

        //转为秒
        data /= 1000
        return s2ms(data)
    }

    /**
     * 将秒格式化为分:秒，例如：150:11
     *
     * @param data
     * @return
     */
    fun s2ms(data: Int): String {
        if (data == 0) {
            return "00:00"
        }

        //计算分钟
        val minute = data / 60

        //秒
        val second = data - minute * 60
        return String.format("%02d:%02d", minute, second)
    }

    /**
     * 将毫秒格式化为时:分:秒，例如：10:20:11
     *
     * @param data
     * @return
     */
    fun ms2hms(data: Int): String {
        if (data == 0) {
            return "00:00:00"
        }
        val formatter = SimpleDateFormat("HH:mm:ss")

        //设置为零时区
        formatter.timeZone = TimeZone.getTimeZone("GMT+00:00")
        return formatter.format(data)
    }

    /**
     * 将ISO8601字符串转为项目中通用的格式
     * 几秒钟前
     * 几天前
     *
     * @param date
     * @return
     */
    fun commonFormat(date: String?): String {
        //将字符串转为DateTime
        val dateTime = DateTime(date)
        return commonFormat(dateTime)
    }

    /**
     * 将DateTime转为项目中通用的格式
     *
     * @param dateTime
     * @return
     */
    private fun commonFormat(dateTime: DateTime): String {
        //计算和现在时间的差
        //单位毫秒
        val value = Date().time - dateTime.toDate().time
        if (value < 1L * ONE_MINUTE) {
            //小于1分钟

            //显示多少秒前
            val data = toSeconds(value)
            return String.format("%d秒前", if (data <= 0) 1 else data)
        } else if (value < 60 * ONE_MINUTE) {
            //小于1小时

            //显示多少分钟前
            val data = toMinutes(value)
            return String.format("%d分钟前", data)
        } else if (value < 24 * ONE_HOUR) {
            //小于1天

            //显示多少小时前
            val data = toHours(value)
            return String.format("%d小时前", data)
        } else if (value < 30 * ONE_DAY) {
            //小于1月

            //显示多少天前
            val data = toDays(value)
            return String.format("%d天前", data)
        }

        //其他时间
        //格式化为yyyyMMddHHmm
        return yyyyMMddHHmm(dateTime)
    }

    /**
     * 将时间戳转为项目中通用的格式
     *
     * @param data
     * @return
     */
    fun commonFormat(data: Long): String {
        //解析时间戳
        val dateTime = DateTime(data)
        return commonFormat(dateTime)
    }

    /**
     * 将DateTime转为yyyy-MM-dd HH:mm
     *
     * @param dateTime
     * @return
     */
    fun yyyyMMddHHmm(dateTime: DateTime): String {
        //格式化日期
        return dateTime.toString(yyyyMMddHHmm)
    }

    /**
     * 将ISO8601字符串转为yyyy-MM-dd HH:mm
     *
     * @param date
     * @return
     */
    fun yyyyMMddHHmm(date: String): String {
        //将字符串转为DateTime
        val dateTime = DateTime(date)
        return yyyyMMddHHmm(dateTime)
    }

    /**
     * 将ISO8601字符串转为yyyy-MM-dd HH:mm:ss
     *
     * @param data
     * @return
     */
    fun yyyyMMddHHmmss(data: String): String {
        //将字符串转为DateTime
        val dateTime = DateTime(data)

        //格式化
        return dateTime.toString(yyyyMMddHHmmss)
    }

    /**
     * 时间戳转为
     *
     * @param data
     * @return
     */
    fun yyyyMMdd(data: Long): String {
        val formatter = SimpleDateFormat(yyyyMMdd)
        return formatter.format(data)
    }

    /**
     * 当前日期转为yyyyMMddHHmmss
     *
     * @return
     */
    fun nowyyyyMMddHHmmss(): String {
        //将字符串转为DateTime
        val dateTime = DateTime()
        return dateTime.toString(yyyyMMddHHmmss)
    }

    /**
     * 转为秒
     *
     * @param date
     * @return
     */
    private fun toSeconds(date: Long): Long {
        return date / 1000L
    }

    /**
     * 转为分钟
     *
     * @param date
     * @return
     */
    private fun toMinutes(date: Long): Long {
        return toSeconds(date) / 60L
    }

    /**
     * 转为小时
     *
     * @param date
     * @return
     */
    private fun toHours(date: Long): Long {
        return toMinutes(date) / 60L
    }

    /**
     * 转为天
     *
     * @param date
     * @return
     */
    private fun toDays(date: Long): Long {
        return toHours(date) / 24L
    }

    /**
     * 转为月
     *
     * @param date
     * @return
     */
    private fun toMonths(date: Long): Long {
        return toDays(date) / 30L
    }

    /**
     * 1分钟毫秒数
     */
    private const val ONE_MINUTE = 60000L

    /**
     * 1小时毫秒数
     */
    private const val ONE_HOUR = 3600000L

    /**
     * 1天毫秒数
     */
    private const val ONE_DAY = 86400000L

    var yyyyMMddHHmm = "yyyy-MM-dd HH:mm"
    var yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
    var HHmmss = "HH:mm"
    var yyyyMMdd = "yyyy-MM-dd"
}