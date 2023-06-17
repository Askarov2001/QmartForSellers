package com.example.qmart.ui.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    var id: String? = null
):Parcelable
