package com.example.news.activity

import android.content.Intent

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
}