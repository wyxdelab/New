package com.example.news.component.login

import com.example.news.entity.Base

/**
 * 登录成功后，返回的信息
 */
class Session(
    /**
     * 用户Id
     */
    var userId: String,
    /**
     * 登录后的Session
     */
    var session: String,
    /**
     * 聊天token
     */
    var chatToken: String
) : Base()