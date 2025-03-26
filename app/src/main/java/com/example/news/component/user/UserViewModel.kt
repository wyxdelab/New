package com.example.news.component.user

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.Constant
import com.example.news.util.DataUtil
import com.example.news.util.PreferenceUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.Locale

class UserViewModel(val style: Int) : BaseViewModel() {
    private lateinit var datum: List<User>
    lateinit var userResult: UserResult
    private val _data = MutableSharedFlow<UserResult>()
    val data: Flow<UserResult> = _data

    private val _title = MutableSharedFlow<Int>()
    val title: Flow<Int> = _title

    fun loadData() {
        if (isFriend) {
            viewModelScope.launch {
                _title.emit(R.string.my_friend)
            }
        } else {
            viewModelScope.launch {
                _title.emit(R.string.my_fans)
            }
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            val r = if (isFriend) {
                DefaultNetworkRepository.friends(PreferenceUtil.getUserId())
            } else {
                DefaultNetworkRepository.fans(PreferenceUtil.getUserId())
            }

            r.onSuccess(viewModel) {
                datum = it!!.data ?: listOf()

                //获取测试数据
                datum = DataUtil.getTestUserData()

                //处理拼音
                datum = DataUtil.processUserPinyin(datum)

                //处理数据
                userResult = DataUtil.processUser(datum)
                _data.emit(userResult)
            }
        }
    }

    fun filter(query: String) {
        if (TextUtils.isEmpty(query)) {
            //没有关键字

            //显示完整数据
            viewModelScope.launch {
                _data.emit(userResult)
            }
        } else {
            //有关键字

            //过滤数据
            val data = DataUtil.filterUser(datum, query.lowercase(Locale.getDefault()))
            viewModelScope.launch {
                _data.emit(DataUtil.processUser(data))
            }
        }
    }


    private val isFriend: Boolean
        private get() = style == Constant.STYLE_FRIEND || style == Constant.STYLE_FRIEND_SELECT
}