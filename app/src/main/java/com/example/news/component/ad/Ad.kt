package com.example.news.component.ad

import android.os.Parcel
import android.os.Parcelable
import com.example.news.entity.Common


/**
 * 广告模型
 */
class Ad : Common {
    /**
     * 标题
     */
    var title: String? = null

    /**
     * 图片
     */
    var icon: String? = null

    /**
     * 点击广告后跳转的地址
     */
    var uri: String? = null

    /**
     * 类型，0：图片；10：视频；20：应用
     */
    var style: Int = 0

    constructor(parcel: Parcel) : super(parcel) {
        title = parcel.readString()
        icon = parcel.readString()
        uri = parcel.readString()
        style = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(title)
        parcel.writeString(icon)
        parcel.writeString(uri)
        parcel.writeInt(style)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Ad> {
        override fun createFromParcel(parcel: Parcel): Ad {
            return Ad(parcel)
        }

        override fun newArray(size: Int): Array<Ad?> {
            return arrayOfNulls(size)
        }
    }

}