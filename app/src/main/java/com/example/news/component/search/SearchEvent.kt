package com.example.news.component.search

/**
 * 搜索事件
 */
class SearchEvent(
    /**
     * 搜索关键字
     */
    var data: String,
    /**
     * 当前显示界面的索引
     */
    var selectedIndex: Int
)