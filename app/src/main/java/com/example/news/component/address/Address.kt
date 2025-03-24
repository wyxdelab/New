package com.example.news.component.address

import com.example.news.entity.Common
import com.example.news.util.Constant
import com.google.gson.annotations.SerializedName

class Address : Common() {
    var name: String? = null
    var telephone: String? = null

    /**
     * 省
     */
    var province: String? = null

    /**
     * 省编码
     */
    var provinceCode: String? = null

    /**
     * 市
     */
    var city: String? = null

    /**
     * 市编码
     */
    var cityCode: String? = null

    /**
     * 区
     */
    var area: String? = null

    /**
     * 区编码
     */
    var areaCode: String? = null

    /**
     * 剩余的地址部分
     */
    var detail: String? = null

    /**
     * 是否是默认地址
     *
     *
     * 0：不是默认
     * 10：默认
     */
    @SerializedName("default")
    var defaultAddress = 0

    /**
     * 获取收货地区
     *
     *
     * 省市区拼接
     *
     * @return
     */
    fun getReceiverArea(): String {
        return String.format("%s%s%s", province, city, area)
    }

    /**
     * 是否是默认地址
     *
     * @return
     */
    fun isDefault(): Boolean {
        return defaultAddress == Constant.VALUE10
    }

    fun getContact(): String? {
        return String.format("%s %s", name, telephone)
    }
}