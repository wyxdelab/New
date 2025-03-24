package com.example.news.component.aliyunoss

/**
 * 密封类，kotlin特有
 */
sealed class UploadResult<out T> {
    /**
     * 成功
     */
    data class Success<out T>(val value: T) : UploadResult<T>()
    data class onProgress<out T>(val progress: Int) : UploadResult<T>()

//    /**
//     * 失败
//     */
//    data class Failure(val throwable: Throwable) : UploadResult<Nothing>()
}

/**
 * 成功
 */
inline fun <reified T> UploadResult<T>.onSuccess(success: (T) -> Unit) {
    if (this is UploadResult.Success) {
        success(value)
    }
}

inline fun <reified T> UploadResult<T>.onProgress(success: (Int) -> Unit) {
    if (this is UploadResult.onProgress) {
        success(progress)
    }
}

/**
 * 异常失败
 */
//inline fun <reified T> UploadResult<T>.onFailure(failure: (Throwable) -> Unit) {
//    if (this is UploadResult.Failure) {
//        failure(throwable)
//    }
//}