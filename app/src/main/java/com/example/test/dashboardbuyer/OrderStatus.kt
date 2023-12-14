package com.example.test.dashboardbuyer

import android.os.Parcel
import android.os.Parcelable


data class OrderStatus(
    val orderId: String = "",
    var buyerId: String = "",
    val status: String = "",
    var totalPrice: String = "",
    var kgQuantity: String = "",
    var fishType: String = "",
    var deliveryName: String = "",
    var deliveryPhone: String = "",
    var deliveryLocation: String = "",
    var sellerId: String = "",
    var selectedItemId: String = "",
    var imageUrl: String = "",
    var phoneNumber: Long? = 0L, // Change the type to Long for whole numbers
    val generatedItemId: String = "",
    var deliveryInfo: String = "",
    var payment: String = "",
    var availability: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
        )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orderId)
        parcel.writeString(buyerId)
        parcel.writeString(status)
        parcel.writeString(totalPrice)
        parcel.writeString(kgQuantity)
        parcel.writeString(fishType)
        parcel.writeString(deliveryName)
        parcel.writeString(deliveryPhone)
        parcel.writeString(deliveryLocation)
        parcel.writeString(sellerId)
        parcel.writeString(selectedItemId)
        parcel.writeString(imageUrl)
        parcel.writeValue(phoneNumber)
        parcel.writeString(generatedItemId)
        parcel.writeString(deliveryInfo)
        parcel.writeString(payment)
        parcel.writeString(availability)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderStatus> {
        override fun createFromParcel(parcel: Parcel): OrderStatus {
            return OrderStatus(parcel)
        }

        override fun newArray(size: Int): Array<OrderStatus?> {
            return arrayOfNulls(size)
        }
    }
}
