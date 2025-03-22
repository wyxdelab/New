package com.example.news.entity.response

import com.example.news.model.BaseViewModel

/**
 * 解析列表网络请求
 */
class ListResponse<T> : BaseResponse() {


    var data: Meta<T>? = null
}

suspend fun <T> ListResponse<T>.onSuccess(viewModel: BaseViewModel, action: suspend(data : Meta<T>?) -> Unit){
    if (isSucceeded) {
        action(data)
    } else {
        viewModel._response.value = this
    }
}