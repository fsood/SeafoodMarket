package com.example.test.dashboardseller

data class OrderManagement (

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
    val generatedItemId: String = ""

)
