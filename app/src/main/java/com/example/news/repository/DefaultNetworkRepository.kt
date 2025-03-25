package com.example.news.repository

import com.example.news.component.ad.Ad
import com.example.news.component.address.Address
import com.example.news.component.api.DefaultNetworkService
import com.example.news.component.cart.Cart
import com.example.news.component.comment.Comment
import com.example.news.component.confirmorder.ConfirmOrderResponse
import com.example.news.component.confirmorder.OrderRequest
import com.example.news.component.content.Content
import com.example.news.component.input.CodeRequest
import com.example.news.component.login.Session
import com.example.news.component.order.Order
import com.example.news.component.pay.PayRequest
import com.example.news.component.pay.PayResponse
import com.example.news.component.product.Product
import com.example.news.component.user.User
import com.example.news.entity.Base
import com.example.news.entity.BaseId
import com.example.news.entity.response.DetailResponse
import com.example.news.entity.response.ListResponse
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 网络数据仓库
 */
object DefaultNetworkRepository {
    private val service: DefaultNetworkService by lazy {
        DefaultNetworkService.create()
    }

    suspend fun contentDetail(id: String): DetailResponse<Content> {
        return service.contentDetail(id)
    }

    suspend fun contents(
        last: String? = null,
        categoryId: String? = null,
        userId: String? = null,
        size: Int = 10,
        style: Int? = null
    ): ListResponse<Content> {
        return service.contents(last, userId, categoryId, size, style)
    }

    suspend fun comments(
        articleId: String? = null,
        parentId: String? = null,
        page: Int = 1,
        size: Int = 10
    ): ListResponse<Comment> {
        return service.comments(articleId, parentId, page, size)
    }

    suspend fun login(data: User): DetailResponse<Session> {
        return service.login(data)
    }

    suspend fun register(data: User): DetailResponse<BaseId> {
        return service.register(data)
    }

    suspend fun userDetail(id: String): DetailResponse<User> {
        return service.userDetail(id)
    }

    suspend fun sendCode(style: Int, data: CodeRequest): DetailResponse<Base> {
        return service.sendCode(style, data)
    }

    suspend fun ads(position: Int = 10, style: Int? = null): ListResponse<Ad> {
        return service.ads(position, style)
    }

    suspend fun createContent(
        data: Content
    ): DetailResponse<BaseId> {
        return service.createContent(data)
    }

    suspend fun productDetail(id: String): DetailResponse<Product> {
        return service.productDetail(id)
    }

    //region 收货地址
    suspend fun addresses(): ListResponse<Address> {
        return service.addresses()
    }

    suspend fun addressDetail(id: String): DetailResponse<Address> {
        return service.addressDetail(id)
    }

    suspend fun createAddress(
        data: Address
    ): DetailResponse<BaseId> {
        return service.createAddress(data)
    }

    suspend fun updateAddress(
        data: Address
    ): DetailResponse<BaseId> {
        return service.updateAddress(data.id!!, data)
    }

    suspend fun deleteAddress(id: String): DetailResponse<Base> {
        return service.deleteAddress(id)
    }

//    suspend fun recognitionAddress(data: DataRequest): DetailResponse<Address> {
//        return service.recognitionAddress(data)
//    }
    //endregion

    //region 订单
    suspend fun confirmOrder(
        data: OrderRequest
    ): DetailResponse<ConfirmOrderResponse> {
        return service.confirmOrder(data)
    }

    suspend fun createOrder(
        data: OrderRequest
    ): DetailResponse<BaseId> {
        return service.createOrder(data)
    }

    suspend fun orderDetail(id: String): DetailResponse<Order> {
        return service.orderDetail(id)
    }

    suspend fun orderPay(id: String, data: PayRequest): DetailResponse<PayResponse> {
        return service.orderPay(id, data)
    }

    suspend fun orders(
        status: Int
    ): ListResponse<Order> {
        return service.orders(status)
    }
    //endregion

    //region 购物车
    suspend fun carts(): ListResponse<Cart> {
        return service.carts()
    }

    suspend fun editCart(
        data: Cart
    ): DetailResponse<Base> {
        return service.editCart(data.id!!, data)
    }

    suspend fun addProductToCart(
        data: Cart
    ): DetailResponse<Base> {
        return service.addProductToCart(data)
    }

    suspend fun deleteCarts(data: List<String>): DetailResponse<Base> {
        return service.deleteCarts(data)
    }
    //endregion
}