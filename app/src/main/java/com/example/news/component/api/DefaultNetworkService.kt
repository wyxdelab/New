package com.example.news.component.api


import com.example.news.component.ad.Ad
import com.example.news.component.address.Address
import com.example.news.component.cart.Cart
import com.example.news.component.category.Category
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
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * 网络API
 */
interface DefaultNetworkService {
    /**
     * 内容列表
     *
     * @return
     */
    @GET("v1/contents")
    suspend fun contents(
        @Query(value = "last") last: String?,
        @Query(value = "category_id") categoryId: String?,
        @Query(value = "user_id") userId: String?,
        @Query(value = "size") size: Int,
        @Query(value = "style") style: Int? = null,
    ): ListResponse<Content>

    /**
     * 内容详情
     */
    @GET("v1/contents/{id}")
    suspend fun contentDetail(@Path("id") id: String): DetailResponse<Content>

    @GET("v1/users/{id}")
    suspend fun userDetail(@Path("id") id: String): DetailResponse<User>

    /**
     * 评论列表
     *
     * @return
     */
    @GET("v1/comments")
    suspend fun comments(
        @Query(value = "article_id") articleId: String?,
        @Query(value = "parent_id") parentId: String?,
        @Query(value = "page") page: Int,
        @Query(value = "size") size: Int
    ): ListResponse<Comment>

    @POST("v1/sessions")
    suspend fun login(
        @Body data: User
    ): DetailResponse<Session>

    @POST("v1/users")
    suspend fun register(
        @Body data: User
    ): DetailResponse<BaseId>

    //region 验证码
    /**
     * 发送验证码
     *
     * @param data
     * @return
     */
    @POST("v1/codes")
    suspend fun sendCode(
        @Query(value = "style") style: Int,
        @Body data: CodeRequest
    ): DetailResponse<Base>
//
//    /**
//     * 校验验证码
//     *
//     * @param data
//     * @return
//     */
//    @POST("v1/codes/check")
//    suspend fun checkCode(
//        @Body data: CodeRequest
//    ): DetailResponse<Base>
//    //endregion

    @GET("v1/ads")
    suspend fun ads(
        @Query(value = "position") position: Int,
        @Query(value = "style") style: Int?
    ): ListResponse<Ad>

    @POST("v1/contents")
    suspend fun createContent(
        @Body data: Content
    ): DetailResponse<BaseId>

    @GET("v1/products/{id}")
    suspend fun productDetail(@Path("id") id: String): DetailResponse<Product>

    //region 收货地址
    @GET("v1/addresses")
    suspend fun addresses(): ListResponse<Address>

    @GET("v1/addresses/{id}")
    suspend fun addressDetail(@Path("id") id: String): DetailResponse<Address>

    @POST("v3/addresses")
    suspend fun createAddress(
        @Body data: Address
    ): DetailResponse<BaseId>

    @PATCH("v1/addresses/{id}")
    suspend fun updateAddress(
        @Path("id") id: String, @Body data: Address
    ): DetailResponse<BaseId>

    @DELETE("v1/addresses/{id}")
    suspend fun deleteAddress(@Path("id") id: String): DetailResponse<Base>

    /**
     * 收货地址文本解析
     *
     * 例如：输入的信息为：四川省成都市天府新区牧华路远大中央公园9栋1单元801 李薇 131411112223
     * 返回为：
     * {
     * "name": "李薇",
     * "phone": "13141111222",
     * "province": "四川省",
     * "province_code": "510000",
     * "city": "成都市",
     * "city_code": "510100",
     * "area": "双流区",
     * "area_code": "510116",
     * "detail": "牧华路远大中央公园9栋1单元801",
     * "default_address": 0
     * }
     *
     * 类似顺丰公众号（大部分快递都有类似的功能），收货地址解析功能
     *
     * 提示：该接口并不是简单的字符串拆分，还会自动纠正省市区信息
     *
     * @return
     */
//    @POST("v1/nlp/address")
//    suspend fun recognitionAddress(@Body data: DataRequest): DetailResponse<Address>
    //endregion

    @PATCH("v1/users/{id}")
    suspend fun updateUser(
        @Path("id") id: String, @Body data: User
    ): DetailResponse<Base>

    @POST("v1/orders/confirm")
    suspend fun confirmOrder(
        @Body data: OrderRequest
    ): DetailResponse<ConfirmOrderResponse>

    @POST("v1/orders")
    suspend fun createOrder(
        @Body data: OrderRequest
    ): DetailResponse<BaseId>

    @GET("v1/orders/{id}")
    suspend fun orderDetail(@Path("id") id: String): DetailResponse<Order>

    @GET("v1/orders")
    suspend fun orders(@Query(value = "status") status: Int): ListResponse<Order>

    @POST("v1/orders/{id}/pay")
    suspend fun orderPay(
        @Path("id") id: String, @Body data: PayRequest
    ): DetailResponse<PayResponse>

//    //region 购物车
    @GET("v1/carts")
    suspend fun carts(): ListResponse<Cart>

    @PATCH("v1/carts/{id}")
    suspend fun editCart(
        @Path("id") id: String, @Body data: Cart
    ): DetailResponse<Base>

    @POST("v1/carts")
    suspend fun addProductToCart(
        @Body data: Cart
    ): DetailResponse<Base>

    @POST("v1/carts/batch_delete")
    suspend fun deleteCarts(@Body data: List<String>): DetailResponse<Base>
//    //endregion
//
    @POST("v1/friends")
    suspend fun follow(
        @Body data: Map<String, String>
    ): DetailResponse<BaseId>

    @DELETE("v1/friends/{id}")
    suspend fun deleteFollow(
        @Path("id") data: String
    ): DetailResponse<BaseId>

    /**
     * 好友列表（我关注的人）
     *
     * @param id
     * @return
     */
    @GET("v1/users/{id}/following")
    suspend fun friends(@Path("id") id: String): ListResponse<User>

    /**
     * 粉丝列表（关注我的人）
     *
     * @param id
     * @return
     */
    @GET("v1/users/{id}/followers")
    suspend fun fans(@Path("id") id: String): ListResponse<User>

    @GET("v1/categories")
    suspend fun categories(@Query(value = "parent") parent: String? = null): ListResponse<Category>

    //region 搜索
    @GET("v1/searches/contents")
    suspend fun searchContent(@QueryMap data: Map<String, String>): ListResponse<Content>

    @GET("v1/searches/users")
    suspend fun searchUser(@QueryMap data: Map<String, String>): ListResponse<User>
//
//    /**
//     * 搜索建议
//     *
//     * @param data
//     * @return
//     */
//    @GET("v1/searches/suggests")
//    suspend fun searchSuggest(@QueryMap data: Map<String, String>): DetailResponse<Suggest>
    //endregion

    companion object {
        fun create(): DefaultNetworkService {
            return NetworkModule.provideRetrofit(NetworkModule.provideOkHttpClient())
                .create(DefaultNetworkService::class.java)
        }
    }
}