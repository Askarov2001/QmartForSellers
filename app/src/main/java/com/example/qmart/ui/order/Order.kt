package com.example.qmart.ui.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class Order(
    var products: List<OrderProduct>? = null,
    var address: String? = null,
    var date: String? = null,
    var time: String? = null,
    var clientName: String? = null,
    var comment: String? = null,
    var bankCard: String? = null,
    var productsCost: Int? = null,
    var deliveryCost: Int? = null,
    var tipCost: Int = 0,
    var totalCost: Int? = null,
    var id: String? = null
)

data class OrderProduct(
    val name: String,
    val image: Int,
    val count: Int,
    val price: Int
)

@Parcelize
data class ClientAddress(
    val city: String,
    val streetHouse: String,
    val apartment: String?,
    val entrance: String?,
    val floor: String?
) : Parcelable
