package com.example.qmart.ui.salepoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.qmart.R
import com.example.qmart.data.SalePointAddress
import com.example.qmart.data.SalePointCashier
import com.example.qmart.databinding.FragmentSalePointBinding
import com.example.qmart.ui.bottomsheet.AddressBottomSheetFragment
import com.example.qmart.ui.bottomsheet.CashierBottomSheetFragment

class SalePointFragment : Fragment() {

    private lateinit var salePointAddress : SalePointAddress
    private lateinit var salePointCashier: SalePointCashier

    companion object {
        fun newInstance() = SalePointFragment()
    }

    private lateinit var binding: FragmentSalePointBinding
    //private lateinit var viewModel: SalePointViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //viewModel = ViewModelProvider(this).get(SalePointViewModel::class.java)
        binding = FragmentSalePointBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setUI()
    }


    private fun setToolbar() {
        requireActivity().apply {
            setActionBar(binding.toolbar)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setUI() = with(binding) {
        chooseAddressTextView.setOnClickListener {
            val fragment = AddressBottomSheetFragment().apply {
                setAddressSelectedListener {
                    salePointAddress = it
                    updateSalePointAddressView()
                }
            }
            fragment.show(parentFragmentManager, "Dialog")
        }

        chooseCashierTextView.setOnClickListener {
            val fragment = CashierBottomSheetFragment().apply {
                setCashierSelectedListener {
                    salePointCashier = it
                    updateSalePointCashierView()
                }
            }
            fragment.show(parentFragmentManager, "Dialog")
        }

        changeAddressButton.setOnClickListener {
            val fragment = AddressBottomSheetFragment().apply {
                setAddressSelectedListener {
                    salePointAddress = it
                    updateSalePointAddressView()
                }
                setAddress(salePointAddress)
            }
            fragment.show(parentFragmentManager, "Dialog")
        }

        deleteCashierButton.setOnClickListener {
            salePointCashier = SalePointCashier("", "")
            cashierLinearLayout.isGone = true
            chooseCashierTextView.isVisible = true
        }

    }

    private fun updateSalePointAddressView() = with(binding) {
        continueButton.setText(R.string.save_sale_point)
        chooseAddressTextView.isGone = true
        pointNameTextView.text = salePointAddress.name
        pointAddressTextView.text = String.format("%s %s", salePointAddress.street, salePointAddress.house)
        addressLinearLayout.isVisible = true
    }

    private fun updateSalePointCashierView() = with(binding) {
        continueButton.setText(R.string.save_sale_point)
        chooseCashierTextView.isGone = true
        cashierPhoneTextView.text = salePointCashier.phone
        cashierNameTextView.text = salePointCashier.name
        cashierLinearLayout.isVisible = true
    }

}