package com.example.news.component.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.component.user.User
import com.example.news.entity.BaseId
import com.example.news.entity.response.DetailResponse
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.StringUtil
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils

/**
 * 注册界面viewmodel
 */
class RegisterViewModel: BaseViewModel() {
    fun register(nickname: String,
                 phone: String,
                 email: String,
                 password: String,
                 confirmPassword: String) {
        if (StringUtils.isBlank(nickname)) {
            _tip.value = R.string.enter_nickname
            return
        }

        //判断昵称格式
        if (!StringUtil.isNickname(nickname)) {
            _tip.value = R.string.error_nickname_format
            return
        }

        //手机号
        if (StringUtils.isBlank(phone)) {
            _tip.value = R.string.enter_phone
            return
        }

        //邮箱
        if (StringUtils.isBlank(email)) {
            _tip.value = R.string.enter_email
            return
        }

        //密码
        if (StringUtils.isBlank(password)) {
            _tip.value = R.string.enter_password
            return
        }

        //密码格式
        if (!StringUtil.isPassword(password)) {
            _tip.value = R.string.error_password_format
            return
        }

        //确认密码
        if (StringUtils.isBlank(confirmPassword)) {
            _tip.value = R.string.enter_confirm_password
            return
        }

        //确认密码格式
        if (!StringUtil.isPassword(confirmPassword)) {
            _tip.value = R.string.error_confirm_password_format
            return
        }

        //判断密码和确认密码是否一样
        if (password != confirmPassword) {
            _tip.value = R.string.error_confirm_password
            return
        }

        val data = User()
        data.nickname = nickname
        data.phone = phone
        data.email = email
        data.password = password

        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.register(data).onSuccess(viewModel) {
                _success.value = it
            }
        }
    }

    private val _success = MutableLiveData<BaseId>()
    val success: LiveData<BaseId> =  _success
}