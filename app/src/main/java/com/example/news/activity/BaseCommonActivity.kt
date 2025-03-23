package com.example.news.activity

import android.content.Intent
import android.text.TextUtils
import com.example.news.util.Constant

/**
 * 通用界面逻辑
 */
open class BaseCommonActivity:BaseActivity() {
    /**
     * 启动界面
     */
    fun startActivity(clazz: Class<*>?) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    /**
     * 启动界面并关闭当前界面
     */
    fun startActivityAfterFinishThis(clazz: Class<*>?) {
        startActivity(clazz)

        finish()
    }

    protected fun startActivityExtraId(
        clazz: Class<*>?,
        id: String
    ) {
        val intent = Intent(this, clazz).apply {
            putExtra(Constant.ID, id)
        }

        startActivity(intent)
    }

    /**
     * 获取字符串
     */
    protected fun extraString(key: String): String {
        return extraStringOrNull(key)!!
    }

    protected fun extraStringOrNull(key: String): String? {
        return intent.getStringExtra(key)
    }
}