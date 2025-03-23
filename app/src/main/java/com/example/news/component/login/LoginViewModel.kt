package com.example.news.component.login

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.component.content.Content
import com.example.news.component.input.InputCodePageData
import com.example.news.component.user.User
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.PreferenceUtil
import com.example.news.util.StringUtil
import com.example.superui.util.SuperRegularUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils

/**
 * 登录界面ViewModel
 */
class LoginViewModel() : BaseViewModel() {

    fun login(username: String, password: String) {
        if (StringUtils.isBlank(username)) {
            _tip.value = R.string.enter_phone_or_email

            //返回
            return
        }

        //如果用户名
        //不是手机号也不是邮箱
        //就是格式错误
        if (!(SuperRegularUtil.isPhone(username) || SuperRegularUtil.isEmail(username))) {
            _tip.value = R.string.error_username_format
            return
        }

        if (TextUtils.isEmpty(password)) {
            _tip.value = R.string.enter_password
            return
        }

        //判断密码格式
        if (!StringUtil.isPassword(password)) {
            _tip.value = R.string.error_password_format
            return
        }

        val user = User()

        //判断是手机号还有邮箱
        if (SuperRegularUtil.isPhone(username)) {
            //手机号
            user.phone = username
        } else {
            //邮箱
            user.email = username
        }

        user.password = password

        login(user)
    }

    /**
     * 用户名和密码，第三方登录
     */
    fun login(data: User) {
        viewModelScope.launch {
            try {
                val r = DefaultNetworkRepository.login(data)
                if (r.isSucceeded) {
                    val it = r.data
                    //保存用户id
                    PreferenceUtil.setUserId(it!!.userId)
                    PreferenceUtil.setToken(it!!.session)

                    //聊天token
                    PreferenceUtil.setChatToken(it!!.chatToken)

                    _success.value = it!!.session

                    //统计登录事件
//                    AnalysisUtil.onLogin(AppContext.instance, true, data)
                } else {
                    _response.value = r

                    //统计登录事件
//                    AnalysisUtil.onLogin(AppContext.instance, false, data)
                }
            } catch (e: Exception) {
                _exception.value = e

                //统计登录事件
//                AnalysisUtil.onLogin(AppContext.instance, false, data)
            }
        }
    }

    /**
     * 验证码登录
     */
    fun login(data: InputCodePageData) {
        val user = User()
        user.phone = data.phone
        user.email = data.email
        user.code = data.code
        login(user)
    }

    //
//    /**
//     * 验证码登录
//     */
//    fun login(data: InputCodePageData) {
//        val user = User()
//        user.phone = data.phone
//        user.email = data.email
//        user.code = data.code
//        login(user)
//    }
//
    //成功
    private val _success = MutableLiveData<String>()
    val success: LiveData<String> = _success
}