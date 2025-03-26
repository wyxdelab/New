package com.example.news.component.user

/**
 * 处理完用户数据模型
 */
class UserResult(
    /**
     * 用来在列表中显示
     */
    var datum: List<Any>,
    /**
     * 字母
     */
    var letters: List<String>,
    /**
     * 字母索引
     */
    var indexes: Array<Int>
)