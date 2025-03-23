package com.example.news.util

import com.tencent.mmkv.MMKV

object PreferenceUtil {
    val p: MMKV by lazy {
        MMKV.defaultMMKV()!!
    }

    /**
     * 是否显示引导界面
     */

    fun isShowGuide(): Boolean {
        return getBoolean(SHOW_GUIDE, true)
    }

    /**
     * 设置是否显示引导界面
     */
    fun setShowGuide(value: Boolean) {
        putBoolean(SHOW_GUIDE, value)
    }

    private fun putBoolean(key: String, value: Boolean) {
        p.edit().putBoolean(key, value).apply()
    }

    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return p.getBoolean(key, defaultValue)
    }

    fun isLogin(): Boolean {
        return Constant.ANONYMOUS != getUserId()
    }
    fun setUserId(value: String?) {
        p.encode(USER_ID, value)
    }

    fun getUserId(): String {
        return p.decodeString(USER_ID, Constant.ANONYMOUS)!!
    }
    /**
     * 设置用户Token
     *
     * 可以加密后存储，防止泄露
     * @param value
     */
    fun setToken(value: String?) {
        p.encode(TOKEN, value)
    }

    /**
     * 获取用户Token
     *
     * @return
     */
    fun getToken(): String? {
        return p.decodeString(TOKEN)
    }

    /**
     * 设置用户Chat Token
     *
     * @param value
     */
    fun setChatToken(value: String) {
        p.encode(CHAT_TOKEN, value)
    }

    /**
     * 获取用户Chat Token
     *
     * @return
     */
    fun getChatToken(): String {
        return p.decodeString(CHAT_TOKEN)!!
    }
    private const val SHOW_GUIDE = "SHOW_GUIDE"

    private const val USER_ID = "user"
    private const val TOKEN = "token"
    private const val CHAT_TOKEN = "CHAT_TOKEN"
}