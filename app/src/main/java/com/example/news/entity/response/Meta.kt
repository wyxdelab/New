package com.example.news.entity.response

/**
 * 解析列表分页
 */
class Meta<T> {
    /**
     * 有多少条
     */
    var total: Int? = null

    /**
     * 有多少页
     */
    var pages: Int? = null

    /**
     * 当前每页显示多少条
     */
    var size: Int? = null

    /**
     * 当前页
     */
    var page: Int? = null

    /**
     * 下一页
     */
    var next: Int? = null

    var data: List<T>? = null

    constructor(data: List<T>?) {
        this.data = data
    }
}