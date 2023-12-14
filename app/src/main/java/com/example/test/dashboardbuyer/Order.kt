package com.example.test.dashboardbuyer

data class Order(
    var totalPrice: String = "",
    var kgQuantity: String = "",
    var fishType: String = "",
    var deliveryName: String = "",
    var deliveryLocation: String = "",
    var deliveryPhone: String = "",
    var buyerId: String = "",
    var sellerId: String = "",
    val generatedItemId: String = "",
    var orderId: String = ""
)
