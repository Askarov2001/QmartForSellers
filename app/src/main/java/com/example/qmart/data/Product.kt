package com.example.qmart.data

import android.net.Uri
import android.os.Parcelable
import com.example.qmart.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    var name: String = "",
    var category: String = "",
    var description: String = "",
    var country: String = "",
    var expirationDate: String = "",
    var packageType: String = "",
    var type: String = "",
    var ingredients: String = "",
    var characteristics: String = "",
    var productImage1: Uri = Uri.parse(""),
    var productImage2: Uri = Uri.parse(""),
    var productImage3: Uri = Uri.parse(""),
    var cost: Int = 0,
    var status: String = ""
): Parcelable
