package com.example.news.config

import com.example.news.BuildConfig

object Config {

    /**
     * 腾讯bugly
     *
     */
    val BUGLY = "acd062b1ac"

    /**
     * 是否是调试模式
     */
    val DEBUG: Boolean = BuildConfig.DEBUG
//    const val DEBUG: Boolean = true

    /**
     * 端点
     */
    const val ENDPOINT = BuildConfig.ENDPOINT

    /**
     * 资源端点
     */
    var RESOURCE_ENDPOINT = BuildConfig.RESOURCE_ENDPOINT

    /**
     * 网络缓存目录大小
     * 100M
     */
    const val NETWORK_CACHE_SIZE = (1024 * 1024 * 100).toLong()

//region 阿里云
    /**
     * 阿里云OSS AK
     */
    const val ALIYUN_AK = "LTAI4Fr1njmWpE4E5uGrMtqk"

    /**
     * 阿里云OSS SK
     */
    const val ALIYUN_SK = "XLCBiAGLN1ad1DWUE3ExAux9lxmRue"

    /**
     * 阿里云OSS Bucket
     */
    const val ALIYUN_OSS_BUCKET_NAME = "dev-courses-misuc"

    /**
     * 阿里云OSS Bucket 地址
     * https://help.aliyun.com/document_detail/31837.html
     */
    const val BUCKET_ENDPOINT = "oss-cn-beijing.aliyuncs.com"
    //endregion

    /**
     * 二维码地址
     * 真实项目中一般设置为应用的下载宣传界面，因为目前没有这样的界面，所以就设置为官网地址
     */
    const val QRCODE_URL = "http://www.ixuea.com"

    /**
     * 用户二维码
     */
    val USER_QRCODE_URL = String.format("%s?u=%%s", QRCODE_URL)

    /**
     * 微信id
     */
    const val WECHAT_AK = "wx672a5ce2ea3a3f4f"

    /**
     * 聊天key
     */
    const val IM_KEY = "cpj2xarlct12n"

    //region 百度语音
    const val BAIDU_VOICE_APP_ID = "33955689"
    const val BAIDU_VOICE_AK = "FcLM001O6FUdq3xs2AvHnwaN"
    const val BAIDU_VOICE_SK = "0gKbr1W6AqvP4dAVbMEHP8kTmE42odn9"
    //endregion
}