package com.example.news.util

import java.util.UUID

/**
 * uuid工具类
 */
object SuperUUIDUtil {//去掉-
    /**
     * 获取uuid
     *
     * @return
     */
    val uuid: String
        get() {
            val uuid = UUID.randomUUID().toString()
            //去掉-
            return uuid.replace("-", "")
        }
}