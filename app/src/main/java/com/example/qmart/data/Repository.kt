package com.example.qmart.data

import android.net.Uri
import com.example.qmart.R
import kotlin.math.cos

object Repository {
    val categories = listOf(
        "Молочные продукты",
        "Рыба и морепродукты",
        "Полуфабрикаты",
        "Хлеб, выпечка",
        "Свежие овощи и фрукты",
        "Фермерская лавка",
        "Колбасы",
        "Яйца"
    )

    val countries = listOf(
        "Казахстан",
        "Россия",
        "Китай",
        "США",
        "Япония"
    )

    val cities = listOf(
        "Алматы",
        "Шымкент",
        "Каскелен",
        "Талдыкорган",
        "Конаев"
    )

    val orderTypes = listOf<Pair<OrderType, Int>>(
        Pair(OrderType.ACTUAL, 0),
        Pair(OrderType.TAKEN, 0)
    )

    val products = ArrayList<Product>()
}

private fun Int.toURI(): Uri {
    return Uri.parse("android.resource://com.example.qmart/$this")
}
