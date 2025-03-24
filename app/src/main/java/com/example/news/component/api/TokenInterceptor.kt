package com.example.news.component.api

import com.example.news.util.PreferenceUtil
import okhttp3.Interceptor
import okhttp3.Response


/**
 * 通过拦截器，实现添加网络请求公共参数
 */
class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取到偏好设置工具类

        //获取到request
        var request = chain.request()

        if (PreferenceUtil.isLogin()) {
            //登录了

            //获取用户token
            val token = PreferenceUtil.getToken()

            //打印日志是方便调试
//            Timber.d("Api token $token")

            request = request.newBuilder()
                .addHeader("Authorization", token!!)
                .build()
        }

        //继续执行网络请求
        val response = chain.proceed(request)
        return response
    }

    companion object {
        const val TAG = "TokenInterceptor"
    }
}