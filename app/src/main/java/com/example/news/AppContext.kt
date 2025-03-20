package com.example.news

import android.app.Application
import android.util.Log
import com.tencent.mmkv.MMKV


/**
 * 全局Application
 */
class AppContext: Application() {
    override fun onCreate() {
        super.onCreate()
        val rootDir = MMKV.initialize(this)
        Log.d(TAG, "initMMKV: $rootDir")
    }

    companion object {
        const val TAG = "AppContext"
    }
}