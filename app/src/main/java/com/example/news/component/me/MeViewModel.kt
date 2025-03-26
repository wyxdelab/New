package com.example.news.component.me

import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.example.news.component.user.User
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.PreferenceUtil

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MeViewModel : BaseViewModel() {
    private val _data = MutableSharedFlow<User>()
    val data: Flow<User> = _data

    fun loadUserData() {
//        if (!NetworkUtils.isAvailableByPing()) {
//            return
//        }

        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.userDetail(PreferenceUtil.getUserId()).onSuccess(viewModel) {
                _data.emit(it!!)
            }
        }
    }

}