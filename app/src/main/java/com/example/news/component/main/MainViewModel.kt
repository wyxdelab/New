package com.example.news.component.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.NetworkUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.example.news.component.user.User
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.util.PreferenceUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils
import java.io.File

/**
 * 首页VM
 */
class MainViewModel : BaseViewModel() {
    private val _userData = MutableSharedFlow<User>()
    val userData: Flow<User> = _userData

    fun loadUserData() {
//        if (!NetworkUtils.isAvailableByPing()) {
//            return
//        }

        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.userDetail(PreferenceUtil.getUserId()).onSuccess(viewModel) {
                _userData.emit(it!!)
            }
        }
    }

//    fun loadSplashAd() {
//        if (NetworkUtils.isAvailableByPing()) {
//            viewModelScope.launch(coroutineExceptionHandler) {
//                DefaultNetworkRepository.ads(style = 0).onSuccess(viewModel) {
//                    if (CollectionUtils.isNotEmpty(it.data)) {
//                        downloadAd(it.data!!.first())
//                    } else {
//                        //删除本地广告数据
//                        deleteSplashAd()
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 删除启动界面广告
//     */
//    private fun deleteSplashAd() {
//        //获取广告信息
//        val ad = PreferenceUtil.getSplashAd()
//
//        ad?.let {
//            //删除配置文件
//            PreferenceUtil.setSplashAd(null)
//
//            //删除文件
//            FileUtils.deleteQuietly(FileUtil.adFile(AppContext.instance, ad.icon!!))
//        }
//    }
//
//    private fun downloadAd(data: Ad) {
////        if (!SuperNetworkUtil.isWifiConnected(AppContext.instance)) {
////            return
////        }
//
//        //wifi才下载
//
//        //判断文件是否存在，如果存在就不下载
//        val targetFile: File = FileUtil.adFile(AppContext.instance, data.icon!!)
//        if (targetFile.exists()) {
//            Log.d(TAG, "downloadAd skip: ${targetFile.absolutePath}")
//            PreferenceUtil.setSplashAd(data)
//            return
//        }
//        Log.d(TAG, "downloadAd: ${targetFile.absolutePath}")
//
//        Thread {
//            try {
//                //FutureTarget会阻塞
//                //所以需要在子线程调用
//                val target: FutureTarget<File> =
//                    Glide.with(AppContext.instance)
//                        .asFile()
//                        .load(ResourceUtil.resourceUri(data.icon!!))
//                        .submit()
//
//                //获取下载的文件
//                val file = target.get()
//
//                //将文件拷贝到我们需要的位置
//                FileUtils.moveFile(file, targetFile)
//
//                PreferenceUtil.setSplashAd(data)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }.start()
//    }
}