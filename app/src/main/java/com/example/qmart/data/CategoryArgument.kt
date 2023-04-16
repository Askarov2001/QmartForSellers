package com.example.qmart.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryArgument (
    val selectedCategory: String,
    val categories: List<String>
) : Parcelable