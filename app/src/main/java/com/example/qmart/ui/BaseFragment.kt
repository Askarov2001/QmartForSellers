package com.example.qmart.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.qmart.MainActivity
import com.example.qmart.R

open class BaseFragment(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {
    protected var baseActivity: MainActivity? = null
    private val navHostFragment: NavHostFragment?
        get() = baseActivity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment_activity_main) as? NavHostFragment
    val navController: NavController?
        get() = navHostFragment?.navController
    open val onBackPressedOverrideCallback: (() -> Unit)? = null
    open val isRootFragment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (activity is MainActivity) {
            baseActivity = activity as MainActivity
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressedOverrideCallback?.let { callback ->
            activity?.onBackPressedDispatcher?.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        callback.invoke()
                    }
                })
        }
        if (isRootFragment) {
            var showSnackBar = true
            activity?.onBackPressedDispatcher?.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (showSnackBar) {
                            showSnackBar = false
                        } else {
                            baseActivity?.finishAndRemoveTask()
                        }
                    }
                })
        }
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    open fun hideKeyboard() {
        val inputManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputManager?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    open fun addProperty(name: String, value: String) {
        baseActivity?.addProperty(name, value)
    }

    open fun getValue(name: String): String? = baseActivity?.getValue(name)

}