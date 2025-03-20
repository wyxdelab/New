package com.example.news.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * 偏好设置工具类
 * 是否登录，是否显示引导页面，用户id
 */
class DefaultPreferenceUtil(context: Context) {
    private var preference: SharedPreferences
    private var context: Context
    init {
        this.context = context.applicationContext

        preference = PreferenceManager.getDefaultSharedPreferences(this.context)
    }
    /**
     * 设置同意了用户协议
     */

    fun setAcceptTermsServiceAgreement() {
        putBoolean(TERMS_SERVICE, true)
    }

    private fun putBoolean(key: String, value: Boolean) {
        preference.edit().putBoolean(key, value).apply()
    }

    /**
     * 获取是否同意了用户条款
     */
    val isAcceptTermsServiceAgreement: Boolean
        get() = preference.getBoolean(TERMS_SERVICE, false)

    companion object {
        private var instance: DefaultPreferenceUtil? = null

        /**
         * 是否同意用户协议的key
         */
        private const val TERMS_SERVICE = "TERMS_SERVICE"

        /**
         * 获取偏好设置单例
         */
        fun getInstance(context: Context): DefaultPreferenceUtil {
            if (instance == null) {
                instance = DefaultPreferenceUtil(context)
            }
            return instance!!
        }
    }

}