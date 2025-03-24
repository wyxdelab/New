package com.ixuea.courses.mymusic.component.productdetail

import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.news.component.product.Product
import com.example.news.entity.response.onSuccess
import com.example.news.model.BaseViewModel
import com.example.news.repository.DefaultNetworkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * 商品详情界面ViewModel
 */
class ProductDetailViewModel(private val id: String) : BaseViewModel() {
    private val _purchasePage = MutableSharedFlow<String>()
    val purchasePage: Flow<String> = _purchasePage

    lateinit var product: Product

    fun loadData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            DefaultNetworkRepository.productDetail(id).onSuccess(viewModel) {
                _data.emit(it!!)
                product = it!!
            }
        }
    }

    fun confirmOrder() {
        viewModelScope.launch {
            _purchasePage.emit(id)
        }
    }
//
//    fun addCart() {
//        val param = Cart()
//        param.productId = id
//        viewModelScope.launch(coroutineExceptionHandler) {
//            DefaultNetworkRepository.addProductToCart(param).onSuccess(viewModel) {
//                _tip.value = R.string.add_cart_success
//            }
//        }
//    }

    private val _data = MutableSharedFlow<Product>()
    val data: Flow<Product> = _data


}