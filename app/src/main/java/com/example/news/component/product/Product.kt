package com.example.news.component.product

import android.os.Parcel
import android.os.Parcelable
import com.example.news.entity.Common

class Product : Common {
    lateinit var title: String
    lateinit var icons: List<String>
    var price: Int = 0
    var highlight: String? = null
    var detail: String? = null

    /**
     * 已经付款的购买人数
     */
    var ordersCount: Long = 0

//    var stocks: List<Stock>? = null
//    var specs: List<Spec>? = null

    val priceFloat
        get() = price * 1.0 / 100

    constructor(parcel: Parcel) : super(parcel) {
        title = parcel.readString().toString()
        icons = parcel.createStringArrayList()!!
        price = parcel.readInt()
        highlight = parcel.readString()
        detail = parcel.readString()
        ordersCount = parcel.readLong()
//        stocks = parcel.createTypedArrayList(Stock)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(title)
        parcel.writeStringList(icons)
        parcel.writeInt(price)
        parcel.writeString(highlight)
        parcel.writeString(detail)
        parcel.writeLong(ordersCount)
//        parcel.writeTypedList(stocks)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Product

        if (title != other.title) return false
        if (icons != other.icons) return false
        if (price != other.price) return false
        if (highlight != other.highlight) return false
        if (detail != other.detail) return false
        if (ordersCount != other.ordersCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + icons.hashCode()
        result = 31 * result + price
        result = 31 * result + (highlight?.hashCode() ?: 0)
        result = 31 * result + (detail?.hashCode() ?: 0)
        result = 31 * result + ordersCount.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }


}