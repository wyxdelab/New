package com.example.news.component.input

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

/**
 * 输入验证码界面数据模型
 */
class InputCodePageData() : Parcelable {
    var style = 0
    var phone: String? = null
    var email: String? = null

    var code: String? = null

    constructor(style: Int) : this() {
        this.style = style
    }

    constructor(parcel: Parcel) : this() {
        style = parcel.readInt()
        phone = parcel.readString()
        email = parcel.readString()
        code = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(style)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeString(code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Creator<InputCodePageData> {
        override fun createFromParcel(parcel: Parcel): InputCodePageData {
            return InputCodePageData(parcel)
        }

        override fun newArray(size: Int): Array<InputCodePageData?> {
            return arrayOfNulls(size)
        }
    }

}