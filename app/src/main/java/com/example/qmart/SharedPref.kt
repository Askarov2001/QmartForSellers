package com.example.qmart

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor


class SharedPref {
    private val STORAGE_NAME = "Preference"
    private var settings: SharedPreferences? = null
    private var editor: Editor? = null
    private var context: Context? = null
    fun init(cntxt: Context?) {
        context = cntxt
    }

    private fun init() {
        settings = context?.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
        editor = settings!!.edit()
    }

    fun addProperty(name: String?, value: String?) {
        if (settings == null) {
            init()
        }
        editor?.putString(name, value)
        editor?.commit()
    }

    fun getProperty(name: String?): String? {
        if (settings == null) {
            init()
        }
        return settings!!.getString(name, null)
    }

    companion object {
        const val NAME = "NAME"
        const val PHONE = "PHONE"
        const val UID = "UID"
        const val MERCHANT = "MERCHANT"
    }
}
