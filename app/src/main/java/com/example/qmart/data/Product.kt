package com.example.qmart.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.UUID

@Parcelize
data class Product(
    var id: String = UUID.randomUUID().toString(),
    var name: String = "",
    var category: Categories = Categories.PRODUCTS,
    var description: String = "",
    var characteristics: String = "",
    var images: String = "",
    var cost: Int = 0,
    var status: String = ACTIVE,
    var sellerId: String? = null,
    var merchant: String? = null
) : Parcelable, Serializable {
    companion object {
        const val INACTIVE = "INACTIVE"
        const val ACTIVE = "ACTIVE"
    }
}
