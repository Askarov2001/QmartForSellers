package com.example.qmart.data

import com.example.qmart.R

enum class Location {
    COUNTRY, CITY
}

enum class OrderType(val title: String) {
    ACTUAL("Самовывоз"),
    TAKEN("Принятые"),
}

enum class ProductType(val title: String) {
    ONSALE("В продаже"),
    SOLD("Сняты с продаж")
}

enum class Categories(val nameRes: Int) {
    PRODUCTS(R.string.products),
    APPLIANCES(R.string.appliances),
    ALL_FOR_HOME(R.string.all_for_home),
    FURNITURE(R.string.furniture),
    ELECTRONIC(R.string.electronic),
    ACCESSORIES(R.string.accessories),
    SPORT(R.string.sport),
    MAKE_UP(R.string.make_up),
    ZOO(R.string.zoo_products);

    companion object {
        fun getByString(value: String): Categories {
            values().forEach {
                if (it.name == value) {
                    return it
                }
            }
            return PRODUCTS
        }
    }
}

enum class STATUS(val status: String) {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    companion object {
        fun getByString(value: String): STATUS {
            values().forEach {
                if (it.name == value) {
                    return it
                }
            }
            return INACTIVE
        }
    }
}