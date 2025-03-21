package com.example.news.entity.response

/**
 * 详情网络请求解析类
 * 继承BaseResponse
 * 定义了一个泛型T
 */
class DetailResponse<T> : BaseResponse() {
    /**
     * 真实数据
     * 类似是泛型
     */
    var data: T? = null
}

