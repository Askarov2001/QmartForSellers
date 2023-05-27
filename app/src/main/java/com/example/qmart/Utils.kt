package com.example.qmart

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.qmart.data.Product

fun openFragment(fragmentManager: FragmentManager, fragment: Fragment, tag: String) {
    fragmentManager.beginTransaction()
        .replace(R.id.container, fragment, tag)
        .addToBackStack(tag)
        .commit()
}

fun EditText.addTextListener(func: (s: String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            func(s.toString())
        }

    })

}
