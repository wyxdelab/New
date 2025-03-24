package com.example.news.component.addressdetail

import android.text.TextUtils
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.component.address.Address
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import com.example.superui.citypicker.Region

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils

/**
 * 商品详情界面ViewModel
 */
class AddressDetailViewModel(private val id: String?) : BaseViewModel() {
    private val _data = MutableSharedFlow<Address>()
    val data: Flow<Address> = _data

    var param: Address? = null

    fun loadData() {
        if (id == null) {
            return
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.addressDetail(id)
                .onSuccess(viewModel) {
                    _data.emit(it!!)
                    param = it!!
                }
        }
    }

    fun delete() {
        if (TextUtils.isEmpty(id)) {
            //如果要实现新建的时候，隐藏删除按钮
            //可以像用户详情那样找到按钮设置
            return
        }

        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.deleteAddress(id!!).onSuccess(viewModel) {
                _tip.value = R.string.delete
                finish()
            }
        }
    }

    fun save(name: String, phone: String, detail: String, defaultAddress: Int) {
        if (TextUtils.isEmpty(name)) {
            _tip.value = R.string.enter_name
            return
        }
        if (TextUtils.isEmpty(phone)) {
            _tip.value = R.string.enter_phone
            return
        }
        if (TextUtils.isEmpty(detail)) {
            _tip.value = R.string.enter_detail_address
            return
        }

        checkCreateAddress()
        param!!.name = name
        param!!.telephone = phone
        param!!.detail = detail
        param!!.defaultAddress = defaultAddress

        viewModelScope.launch(coroutineExceptionHandler) {
            if (StringUtils.isNotBlank(id)) {
                DefaultNetworkRepository.updateAddress(param!!)
            } else {
                DefaultNetworkRepository.createAddress(param!!)
            }.onSuccess(viewModel) {
                finish()
            }
        }
    }
//
    private fun checkCreateAddress() {
        if (param == null) {
            param = Address()
        }
    }



    fun setCity(province: Region, city: Region, area: Region) {
        checkCreateAddress()

        //设置数据
        //省
        param!!.province = province.getName()
        param!!.provinceCode = province.getId().toString()

        //市
        param!!.city = city.getName()
        param!!.cityCode = city.getId().toString()

        //区
        param!!.area = area.getName()
        param!!.areaCode = area.getId().toString()

        viewModelScope.launch {
            _data.emit(param!!)
        }
    }

//    fun recognitionAddress(data: String) {
//        if (StringUtils.isBlank(data)) {
//            _tip.value = R.string.enter_content
//            return
//        }
//
//        viewModelScope.launch(coroutineExceptionHandler) {
//            DefaultNetworkRepository.recognitionAddress(DataRequest(data)).onSuccess(viewModel) {
//                _data.emit(it!!)
//                param = it!!
//            }
//        }
//    }
}