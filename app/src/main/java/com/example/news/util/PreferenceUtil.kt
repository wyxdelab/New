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
    private const val SHOW_GUIDE = "SHOW_GUIDE"
}