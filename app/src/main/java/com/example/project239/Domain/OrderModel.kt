
package com.example.project239.domain

data class OrderModel(
    var orderId: String = "",
    var orderDate: Long = 0L,
    var items: ArrayList<ItemsModel> = arrayListOf(),
    var totalPrice: Double = 0.0,
    var status: String = "Pending"
)
