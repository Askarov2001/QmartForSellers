package com.example.qmart.ui.order

data class OrderProduct(
    val name: String,
    val image: String?,
    val count: Int,
    val price: Int
) {
    constructor() : this("", "", 0, 0)
}