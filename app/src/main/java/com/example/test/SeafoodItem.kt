package com.example.test

import android.os.Parcel
import android.os.Parcelable


data class SeafoodItem(
    val vendorName: String = "",
    val location: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val deliveryInfo: String = "",
    var availability: String = "",
    val phoneNumber: Long? = 0L, // Change the type to Long for whole numbers
    val fishType: String = "",
    val source: String = "",
    val payment: String = "", // Change the type to Double for payment
    val sellerId: String = "",
    val generatedItemId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(vendorName)
        parcel.writeString(location)
        parcel.writeDouble(price)
        parcel.writeString(imageUrl)
        parcel.writeString(deliveryInfo)
        parcel.writeString(availability)
        parcel.writeValue(phoneNumber)
        parcel.writeString(fishType)
        parcel.writeString(source)
        parcel.writeString(payment)
        parcel.writeString(sellerId)
        parcel.writeString(generatedItemId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SeafoodItem> {
        override fun createFromParcel(parcel: Parcel): SeafoodItem {
            return SeafoodItem(parcel)
        }

        override fun newArray(size: Int): Array<SeafoodItem?> {
            return arrayOfNulls(size)
        }
    }
}
