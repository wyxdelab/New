package com.example.news.entity.response

import com.example.news.model.BaseViewModel

/**
 * 详情网络请求解析类
 * 继承BaseResponse
 * 定义了一个泛型T
 */
class DetailResponse<T> : BaseResponse() {
    /**
     * 真实数据
     * 类似是泛型
     */
    var data: T? = null
}

suspend fun <T> DetailResponse<T>.onSuccess(viewModel: BaseViewModel, action: suspend(data : T?) -> Unit){
    if (isSucceeded) {
        action(data)
    } else {
        viewModel._response.value = this
    }
}

