package com.example.news.activity

import com.blankj.utilcode.util.NetworkUtils
import com.example.news.AppContext
import com.example.news.R
import com.example.news.entity.response.BaseResponse
import com.example.news.model.BaseViewModel
import com.example.news.util.PreferenceUtil
import com.example.superui.extension.longToast
import com.example.superui.extension.shortToast
import com.ixuea.superui.extension.longToast
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 本项目的通用逻辑，例如：背景颜色等
 */
open class BaseLogicActivity:BaseCommonActivity() {

    protected val hostActivity: BaseLogicActivity
        protected get() = this

    /**
     * 初始化通用ViewModel逻辑
     */
    protected fun initViewModel(viewModel: BaseViewModel) {
        //关闭界面
        viewModel.finishPage.observe(this) {
            hostActivity.finish()
        }

        //本地提示
        viewModel.tip.observe(this) {
            onTip(it)
        }

        //异常
        viewModel.exception.observe(this) {
            onException(it)
        }

        //网络响应业务失败
        viewModel.response.observe(this) {
            onResponse(it)
        }

        //加载提示
        viewModel.loading.observe(this) {
        }
    }

    open fun onTip(data: Int) {
        data.shortToast()
        onError()
    }

    open fun onError() {

    }

    open fun onResponse(data: BaseResponse) {
        when (data.status) {
            401 -> {
                R.string.error_not_auth.longToast()
                AppContext.instance.logout()
            }

            403 -> {
                R.string.error_not_permission.longToast()
            }

            404 -> {
                R.string.error_not_found.longToast()
            }
        }
        (data.message ?: getString(R.string.error_unknown)).longToast()
        onError()
    }
    open fun onException(data: Throwable) {
        if (NetworkUtils.isAvailableByPing()) {
            //有网络
            R.string.error_load.longToast()
        } else {
            //提示，你的网络好像不太好
            R.string.error_network_not_connect.longToast()
        }


        onError()
    }
//    fun logout() {
//        logoutSilence()
//    }
//
//    private fun logoutSilence() {
//        //清除登录相关信息
//        PreferenceUtil.logout()
//
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
//
//        loginStatusChanged()
//    }
}