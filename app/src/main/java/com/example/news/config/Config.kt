package com.example.news.config

import com.example.news.BuildConfig

object Config {

    val DEBUG: Boolean = BuildConfig.DEBUG

    /**
     * 端点
     */
    const val ENDPOINT = BuildConfig.ENDPOINT

    /**
     * 资源端点
     */
    var RESOURCE_ENDPOINT = BuildConfig.RESOURCE_ENDPOINT

    /**
     * 网络缓存目录大小 100M
     */
    const val NETWORK_CACHE_SIZE = (1024 * 1024 * 100).toLong()
}