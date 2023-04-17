package com.example.qmart.data

import android.net.Uri
import android.os.Parcelable
import com.example.qmart.R
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
    var images: ArrayList<String> = ArrayList(),
    var cost: Int = 0,
    var status: String = ""
) : Parcelable,Serializable
