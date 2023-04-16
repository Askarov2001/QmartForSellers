package com.example.qmart.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import com.example.qmart.R
import com.example.qmart.data.Location
import com.example.qmart.databinding.FragmentCountryBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CountryBottomSheetFragment : BottomSheetDialogFragment() {

    private var selectedIndex = -1
    private var onCountrySelectedListener: (Pair<Int, String>) -> Unit = {}
    private var countries: List<String> = emptyList()
    private var type: Location = Location.COUNTRY

    lateinit var binding: FragmentCountryBottomSheetBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        binding = FragmentCountryBottomSheetBinding.inflate(layoutInflater)
        val view = binding.root
        bottomSheet.setContentView(view)

        setBottomSheet()
        setUI()

        return bottomSheet
    }

    @SuppressLint("InternalInsetResource")
    private fun setBottomSheet() {
        val view = binding.root
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }

        val params = view.layoutParams
        val tv = TypedValue()
        var actionBarHeight = 0
        if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
        var statusBarHeight: Int = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        params.height = Resources.getSystem().displayMetrics.heightPixels - actionBarHeight - statusBarHeight
        view.layoutParams = params
    }

    private fun setUI() = with(binding) {
        val countriesAdapter = CategoryAdapter()

        titleTextView.setText(
            when(type){
                Location.COUNTRY -> R.string.choose_country_title
                Location.CITY -> R.string.choose_city_title
            }
        )

        closeButton.setOnClickListener {
            dialog?.dismiss()
        }

        recyclerView.apply {
            adapter = countriesAdapter
        }

        chooseButton.setOnClickListener {
            onCountrySelectedListener.invoke(
                Pair(
                    countriesAdapter.getSelectedIndex(),
                    countriesAdapter.getSelectedName()
                )
            )
            dialog?.dismiss()
        }

        countriesAdapter.apply {
            setSelectedIndex(selectedIndex)
            submitList(countries)
        }
    }

    fun setCountries(index: Int, list: List<String>, type: Location) {
        selectedIndex = index
        countries = list
        this.type = type
    }

    fun setCategorySelectedListener(onCountrySelectedListener: (Pair<Int, String>) -> Unit) {
        this.onCountrySelectedListener = onCountrySelectedListener
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }
}