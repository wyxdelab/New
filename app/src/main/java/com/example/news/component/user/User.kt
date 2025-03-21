package com.example.news.component.user

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import com.example.news.entity.Common

class User: Common() {
    /**
     * 昵称
     */
    var nickname: String? = null

    /**
     * 头像
     */
    var icon: String? = null

    /**
     * 手机号
     */
    var phone: String? = null

    /**
     * 邮箱
     */
    var email: String? = null

    /**
     * 用户的密码,登录，注册向服务端传递
     */
    var password: String? = null

    /**
     * 微信第三方登录后id
     */
    var wechatId: String? = null

    /**
     * QQ第三方登录后Id
     */
    var qqId: String? = null

    /**
     * 验证码
     * 只有找回密码的时候才会用到
     */
    var code: String? = null

    /**
     * 描述
     */
    var detail: String? = null

    /**
     * 省
     */
    var province: String? = null

    /**
     * 省编码
     */
    var provinceCode: String? = null

    /**
     * 市
     */
    var city: String? = null

    /**
     * 市编码
     */
    var cityCode: String? = null


    /**
     * 区
     */
    var area: String? = null

    /**
     * 区编码
     */
    var areaCode: String? = null

    /**
     * 我的关注的人（好友）
     */
    var followingsCount: Long = 0

    /**
     * 关注我的人（粉丝）
     */
    var followersCount: Long = 0

    /**
     * 是否关注
     * 1:关注
     * 在用户详情才会返回
     */
    var following: String? = null

    /**
     * 性别
     * 0：保密，10：男，20：女
     * 可以定义为枚举
     * 但枚举性能没有int好
     * 但int没有一些编译验证
     * Android中有替代方式
     * 这里用不到就不讲解了
     */
    var gender = 0

    /**
     * 生日
     * 格式为：yyyy-MM-dd
     */
    var birthday: String? = null

    /**
     * 设备名称
     * 例如：小米11
     */
    var device: String? = null


    /**
     * 推送id
     */
    var push: String? = null

    //region 本地过滤
    /**
     * 拼音
     */
    var pinyin: String? = null

    /**
     * 拼音首字母
     */
    var pinyinFirst: String? = null

    /**
     * 拼音首字母的首字母
     */
    var first: String? = null

    //endregion
//
//    constructor() : super()
//
//    constructor(parcel: Parcel) : super(parcel) {
//        nickname = parcel.readString()
//        icon = parcel.readString()
//        phone = parcel.readString()
//        email = parcel.readString()
//        password = parcel.readString()
//        wechatId = parcel.readString()
//        qqId = parcel.readString()
//        code = parcel.readString()
//        detail = parcel.readString()
//        province = parcel.readString()
//        provinceCode = parcel.readString()
//        city = parcel.readString()
//        cityCode = parcel.readString()
//        area = parcel.readString()
//        areaCode = parcel.readString()
//        followingsCount = parcel.readLong()
//        followersCount = parcel.readLong()
//        following = parcel.readString()
//        gender = parcel.readInt()
//        birthday = parcel.readString()
//        device = parcel.readString()
//        push = parcel.readString()
//        pinyin = parcel.readString()
//        pinyinFirst = parcel.readString()
//        first = parcel.readString()
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        super.writeToParcel(parcel, flags)
//        parcel.writeString(nickname)
//        parcel.writeString(icon)
//        parcel.writeString(phone)
//        parcel.writeString(email)
//        parcel.writeString(password)
//        parcel.writeString(wechatId)
//        parcel.writeString(qqId)
//        parcel.writeString(code)
//        parcel.writeString(detail)
//        parcel.writeString(province)
//        parcel.writeString(provinceCode)
//        parcel.writeString(city)
//        parcel.writeString(cityCode)
//        parcel.writeString(area)
//        parcel.writeString(areaCode)
//        parcel.writeLong(followingsCount)
//        parcel.writeLong(followersCount)
//        parcel.writeString(following)
//        parcel.writeInt(gender)
//        parcel.writeString(birthday)
//        parcel.writeString(device)
//        parcel.writeString(push)
//        parcel.writeString(pinyin)
//        parcel.writeString(pinyinFirst)
//        parcel.writeString(first)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }

    /**
     * 是否关注了
     *
     * @return
     */
    fun isFollowing(): Boolean {
        return following != null
    }

    /**
     * 格式化后的性别
     *
     * @return
     */
    fun getGenderFormat(): String? {
        return when (gender) {
            10 -> "男"
            20 -> "女"
            else ->                 //0
                "保密"
        }
    }

//    fun birthdayFormat(): String? {
//        return if (StringUtils.isBlank(birthday)) {
//            ""
//        } else birthday
//    }

    /**
     * 格式化后的描述
     *
     * @return
     */
    fun getDetailFormat(): String? {
        return if (TextUtils.isEmpty(detail)) {
            "这个人很懒，没有填写个人介绍!"
        } else detail
    }

    companion object {
        /**
         * 未知
         */
        const val GENDER_UNKNOWN = 0

        /**
         * 男
         */
        const val MALE = 10

        /**
         * 女
         */
        const val FEMALE = 20

//        @JvmField
//        val CREATOR = object : Parcelable.Creator<User> {
//            override fun createFromParcel(parcel: Parcel): User {
//                return User(parcel)
//            }
//
//            override fun newArray(size: Int): Array<User?> {
//                return arrayOfNulls(size)
//            }
//        }
    }
}