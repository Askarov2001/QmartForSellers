package com.example.qmart.data

data class SalePoint (
    val address: SalePointAddress,
    val cashier: SalePointCashier
)

data class SalePointAddress(
    val name: String,
    val city: String,
    val street: String,
    val house: String
)

data class SalePointCashier(
    val name: String,
    val phone: String
)