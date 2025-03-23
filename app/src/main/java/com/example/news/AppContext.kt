package com.example.news

import android.app.Application
import android.util.Log
import com.drake.channel.sendEvent
import com.example.news.component.login.LoginStatusChangedEvent
import com.example.news.util.PreferenceUtil
import com.tencent.mmkv.MMKV


/**
 * 全局Application
 */
class AppContext: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        val rootDir = MMKV.initialize(this)
        Log.d(TAG, "initMMKV: $rootDir")
    }

    fun logout() {
        logoutSilence()
    }

    private fun logoutSilence() {
        //清除登录相关信息
        PreferenceUtil.logout()

//        //清除第三方登录信息
//        otherLogout(Wechat.NAME)
//        otherLogout(QQ.NAME)
//
//        //退出聊天服务器
//        isLoginChat = false
//        RongCoreClient.getInstance().logout()
//
//        //销毁一些实例，用的实例在获取，这样获取的用户id就是新的用户
//        LiteORMUtil.destroy()
//
//        //下载管理器
//        if (DownloadService.downloadManager != null) {
//            DownloadService.downloadManager.destroy()
//            DownloadService.downloadManager = null
//        }

        loginStatusChanged()
    }

    private fun loginStatusChanged() {
        sendEvent(LoginStatusChangedEvent())
    }

    fun onLogin() {
        loginStatusChanged()
    }

    companion object {
        const val TAG = "AppContext"

        lateinit var instance: AppContext
    }
}