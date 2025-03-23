package com.example.news.component.input

import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.model.BaseViewModel
import com.example.news.util.Constant
import com.example.superui.util.SuperRegularUtil

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class InputIdentityViewModel(private val style: Int) : BaseViewModel() {
    private val _title = MutableSharedFlow<Int>()
    val title: Flow<Int> = _title

    private val _toNext = MutableSharedFlow<InputCodePageData>()
    val toNext: Flow<InputCodePageData> = _toNext

    fun loadData() {
        viewModelScope.launch {
            val r = when (style) {
                Constant.STYLE_CODE_LOGIN -> {
                    //验证码登录
                    R.string.code_login
                }

                else -> {
                    //找回密码
                    R.string.forgot_password
                }
            }
            _title.emit(r)
        }
    }

    fun primaryClick(data: String) {
        val isPhone: Boolean = SuperRegularUtil.isPhone(data)
        if (isPhone || SuperRegularUtil.isEmail(data)) {
            val pageData = InputCodePageData(style)
            if (isPhone) {
                pageData.phone = data
            } else {
                pageData.email = data
            }

            viewModelScope.launch {
                _toNext.emit(pageData)
            }
        } else {
            _tip.value = R.string.error_username_format

        }
    }
}