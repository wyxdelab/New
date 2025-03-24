package com.example.news.component.publish

import androidx.lifecycle.viewModelScope
import com.example.news.AppContext
import com.example.news.R
import com.example.news.component.content.Content
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import com.example.news.component.aliyunoss.AliyunOSSService
import com.example.news.component.aliyunoss.onProgress
import com.example.news.component.aliyunoss.onSuccess
import com.luck.picture.lib.entity.LocalMedia

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import timber.log.Timber

/**
 * 发布动态界面ViewModel
 */
class PublishViewModel() : BaseViewModel() {
    private lateinit var medias: List<LocalMedia>
    private var uploadMedias: List<String>? = null

    private val _success = MutableSharedFlow<Boolean>()
    val success: Flow<Boolean> = _success

    private val _data = MutableSharedFlow<List<Any>>()
    val data: Flow<List<Any>> = _data

//    protected val _selectPosition = MutableLiveData<Any>()
//    val selectPosition: LiveData<Any> = _selectPosition

    private val param: Content = Content()

    fun sendClick(content: String, medias: List<LocalMedia>) {
        this.medias = medias
        param.content = content
        //判断是否输入了
        if (StringUtils.isBlank(content)) {
            _tip.value = R.string.hint_content
            return
        }

        //判断长度
        if (content.length > 1000) {
            _tip.value = R.string.error_content_length
            return
        }

        //获取选中的图片
        if (medias.isNotEmpty()) {
            //有图片

            //先上传图片
            uploadMedia()
        } else {
            //没有图片

            //直接发布
        save()
        }
    }

    private fun uploadMedia() {
        viewModelScope.launch(coroutineExceptionHandler) {
            AliyunOSSService.getInstance(AppContext.instance)
                .upload(medias)
                .collectLatest {
                    it.onProgress {
                        Timber.d("upload media progress %d", it)
                        _loading.value =
                            AppContext.instance.getString(R.string.loading_upload, it + 1)
                    }
                    it.onSuccess {
                        uploadMedias = it
                        _loading.value = null
                        save()
                    }
                }
        }
    }

    private fun save() {
        uploadMedias?.let {
            param.icon = it.joinToString(",")
        }
//
//        (selectPosition.value as? PoiItem)?.let {
//            //地理位置信息
//            //经度
////            param.setLongitude(selectPosition.getLatLonPoint().getLongitude())
////
////            //纬度
////            param.setLatitude(selectPosition.getLatLonPoint().getLatitude())
//
//            //省
//            param.province = "${it.cityName} . ${it.title}"
//        }

        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.createContent(param).onSuccess(viewModel) {
                _success.emit(true)
            }
        }
    }

    fun loadData() {
        setData(ArrayList())
    }
//
//    fun setLocation(data: Any) {
//        _selectPosition.value = data
//    }
//
    fun setData(datum: MutableList<Any>) {
        if (datum.size < 9) {
            //添加选择图片按钮
            datum.add(R.drawable.add_fill)
        }

        viewModelScope.launch {
            _data.emit(datum)
        }
    }
}