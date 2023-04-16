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
        Pair(OrderType.PICKUP, 0),
        Pair(OrderType.ONDELIVERY, 0),
        Pair(OrderType.PACKAGING, 0),
        Pair(OrderType.TRANSFER, 0),
        Pair(OrderType.WAITDELIVERY, 0),
        Pair(OrderType.ARCHIVE, 0),
    )

    val products = listOf(
        Product(
            name = "Домашние печеньки овсяное",
            category = "Хлеб, выпечка",
            cost = 776,
            description = "Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой.",
            country = countries[0],
            expirationDate = "12",
            packageType = "коробка картонная",
            type = "бисквитное",
            ingredients = "овсянка",
            characteristics = "с глазурью",
            productImage1 = R.drawable.image_cookie.toURI()
        ),
        Product(
            name = "Пончики",
            cost = 376,
            description = "Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой.",
            country = countries[0],
            expirationDate = "12",
            packageType = "коробка картонная",
            type = "бисквитное",
            ingredients = "овсянка",
            characteristics = "с глазурью",
            productImage1 = R.drawable.image_donuts.toURI()
        ),
        Product(
            name = "Бауырсак",
            cost = 700,
            description = "Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой.",
            country = countries[0],
            expirationDate = "12",
            packageType = "коробка картонная",
            type = "бисквитное",
            ingredients = "овсянка",
            characteristics = "с глазурью",
            productImage1 = R.drawable.image_baursaq.toURI()
        ),
        Product(
            name = "Микс зелени",
            cost = 776,
            description = "Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой.",
            country = countries[0],
            expirationDate = "12",
            packageType = "коробка картонная",
            type = "бисквитное",
            ingredients = "овсянка",
            characteristics = "с глазурью",
            productImage1 = R.drawable.image_mix_salads.toURI()
        ),
        Product(
            name = "Колбаса Салам",
            cost = 2776,
            description = "Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой.",
            country = countries[0],
            expirationDate = "12",
            packageType = "коробка картонная",
            type = "бисквитное",
            ingredients = "овсянка",
            characteristics = "с глазурью",
            productImage1 = R.drawable.image_sausage.toURI()
        ),
        Product(
            name = "Кокосовое молоко",
            cost = 676,
            description = "Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой. Очень вкусные пончики к чаю с шоколадной начинкой.",
            country = countries[0],
            expirationDate = "12",
            packageType = "коробка картонная",
            type = "бисквитное",
            ingredients = "овсянка",
            characteristics = "с глазурью",
            productImage1 = R.drawable.image_coconut_milk.toURI()
        ),
    )
}

private fun Int.toURI(): Uri {
    return Uri.parse("android.resource://com.example.qmart/$this")
}
