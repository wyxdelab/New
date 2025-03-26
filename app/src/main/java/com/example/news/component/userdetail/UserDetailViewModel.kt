package com.example.news.component.userdetail

import androidx.lifecycle.viewModelScope
import com.example.news.component.user.User
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(val id: String) : BaseViewModel() {
    private val _data = MutableSharedFlow<User>()
    val data: Flow<User> = _data

    private lateinit var d: User

    fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.userDetail(id).onSuccess(viewModel) {
                delay(800)
                _data.emit(it!!)
                d = it!!
            }
        }
    }

    fun follow() {
        if (d.isFollowing()) {
            //已经关注了

            //取消关注
            viewModelScope.launch {
                DefaultNetworkRepository.deleteFollow(id).onSuccess(viewModel) {
                    d.following = null
                    _data.emit(d)
                }
            }

        } else {
            //没有关注

            //关注
            viewModelScope.launch {
                DefaultNetworkRepository.follow(id).onSuccess(viewModel) {
                    d.following = "1"
                    _data.emit(d)
                }
            }
        }
    }
}