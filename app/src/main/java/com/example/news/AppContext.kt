package com.example.news

import android.app.Application
import android.util.Log
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import com.drake.channel.sendEvent
import com.example.news.component.liteorm.LiteORMUtil
import com.example.news.component.login.LoginStatusChangedEvent
import com.example.news.config.Config
import com.example.news.util.PreferenceUtil
import com.tencent.mmkv.MMKV
import timber.log.Timber


/**
 * 全局Application
 */
class AppContext: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        initLog()
        val rootDir = MMKV.initialize(this)
        Log.d(TAG, "initMMKV: $rootDir")

        var config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
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
        //销毁一些实例，用的实例在获取，这样获取的用户id就是新的用户
        LiteORMUtil.destroy()
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
    private fun initLog() {
        if (Config.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            //可以上报到任何地方，真实项目大部分都会用第三方日志服务
            //例如：阿里云日志服务，或者自己公司有；打印到文件那种现在用的太少了，所以也就不实现了

        }
    }
    companion object {
        const val TAG = "AppContext"

        lateinit var instance: AppContext
    }
}