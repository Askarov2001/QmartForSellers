package com.example.qmart.data

enum class Location {
    COUNTRY, CITY
}

enum class OrderType(val title: String) {
    PICKUP("Самовывоз"),
    ONDELIVERY("На доставке"),
    PACKAGING("Упаковка"),
    TRANSFER("Передача"),
    WAITDELIVERY("Ожидают доставки"),
    ARCHIVE("Архив")
}

enum class ProductType(val title: String) {
    ONSALE("В продаже"),
    SOLD("Сняты с продаж")
}