package com.example.news.component.profile

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.drake.channel.sendEvent
import com.example.news.AppContext
import com.example.news.R
import com.example.news.component.aliyunoss.AliyunOSSService
import com.example.news.component.user.User
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.PreferenceUtil
import com.example.news.util.StringUtil
import com.example.superui.citypicker.Region

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ProfileViewModel() : BaseViewModel() {
    private val _data = MutableSharedFlow<User>()
    val data: Flow<User> = _data

    lateinit var param: User

    fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.userDetail(PreferenceUtil.getUserId()).onSuccess(viewModel) {
                _data.emit(it!!)
                param = it
            }
        }
    }

    fun save(nickname: String, detail: String) {
        //判断是否输入昵称
        if (TextUtils.isEmpty(nickname)) {
            _tip.value = R.string.enter_nickname
            return
        }
        if (!StringUtil.isNickname(nickname)) {
            _tip.value = R.string.error_nickname_format
            return
        }

        param.nickname = nickname
        param.detail = detail

        //更新资料
        updateUserInfo()
    }

    private fun updateUserInfo() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.updateUser(param).onSuccess(viewModel) {
                finish()

                //更新成功
                //不提示任何信息

                //关闭当前界面
                //当然也可以不关闭
                //真实项目中根据业务需求调整就行了
                sendEvent(UserInfoChanged())
            }
        }
    }

    fun upload(data: String) {
        param.icon = null

        viewModelScope.launch(coroutineExceptionHandler) {
            param.icon = AliyunOSSService.getInstance(AppContext.instance).upload(data)

            //更新资料
            updateUserInfo()
        }
    }

    fun setGender(data: Int) {
        viewModelScope.launch {
            param.gender = data
            _data.emit(param)
        }
    }

    fun setBirthday(data: String) {
        viewModelScope.launch {
            param.birthday = data
            _data.emit(param)
        }
    }

    fun setArea(province: Region, city: Region, area: Region) {
        viewModelScope.launch {
            param.province = province.name
            param.provinceCode = province.id.toString()

            param.city = city.name
            param.cityCode = city.id.toString()

            param.area = area.name
            param.areaCode = area.id.toString()
            _data.emit(param)
        }
    }
}