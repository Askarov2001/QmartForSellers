package com.example.qmart.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.example.qmart.R
import com.example.qmart.data.Location
import com.example.qmart.data.Repository
import com.example.qmart.data.SalePointAddress
import com.example.qmart.databinding.FragmentAddressBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddressBottomSheetFragment : BottomSheetDialogFragment() {

    private var selectedIndex = -1
    private var selectedCity = EMPTY
    private var selectedAddress: SalePointAddress? = null

    private var onAddressSelectedListener: (SalePointAddress) -> Unit = {}

    lateinit var binding: FragmentAddressBottomSheetBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        binding = FragmentAddressBottomSheetBinding.inflate(layoutInflater)
        val view = binding.root
        bottomSheet.setContentView(view)

        setBottomSheet()
        setUI()

        return bottomSheet
    }

    private fun setBottomSheet() {
        val view = binding.root
        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }

        //val params = view.layoutParams
        //params.height = Resources.getSystem().displayMetrics.heightPixels
        //view.layoutParams = params
    }

    private fun setUI() = with(binding) {
        closeButton.setOnClickListener {
            dialog?.dismiss()
        }

        chooseCityTextView.setOnClickListener {
            val fragment = CountryBottomSheetFragment().apply {
                setCategorySelectedListener {
                    selectedIndex = it.first
                    selectedCity = it.second
                    updateCityView()
                }
                setCountries(
                    selectedIndex,
                    Repository.cities,
                    Location.CITY
                )
            }
            fragment.show(parentFragmentManager, "Dialog")
        }

        chooseButton.setOnClickListener {
            onAddressSelectedListener.invoke(
                SalePointAddress(
                    name = salePointNameEditText.text.toString(),
                    city = chooseCityTextView.text.toString(),
                    street = salePointStreetEditText.text.toString(),
                    house = salePointHouseEditText.text.toString()
                )
            )
            dialog?.dismiss()
        }
        val address = selectedAddress
        if(address != null){
            salePointNameEditText.setText(address.name)
            chooseCityTextView.text = address.city
            salePointStreetEditText.setText(address.street)
            salePointHouseEditText.setText(address.house)
        }
    }

    fun setAddressSelectedListener(onAddressSelectedListener: (SalePointAddress) -> Unit) {
        this.onAddressSelectedListener = onAddressSelectedListener
    }

    fun setAddress(address: SalePointAddress) {
        selectedAddress = address
    }

    private fun updateCityView() {
        binding.chooseCityTextView.text = if (selectedCity != EMPTY) selectedCity else resources.getString(R.string.city)
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }
}