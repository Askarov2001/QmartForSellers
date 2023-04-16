package com.example.qmart.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.example.qmart.R
import com.example.qmart.data.SalePointCashier
import com.example.qmart.databinding.FragmentCashierBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CashierBottomSheetFragment : BottomSheetDialogFragment() {

    private var onCashierSelectedListener: (SalePointCashier) -> Unit = {}

    lateinit var binding: FragmentCashierBottomSheetBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        binding = FragmentCashierBottomSheetBinding.inflate(layoutInflater)
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
    }

    private fun setUI() = with(binding) {
        closeButton.setOnClickListener {
            dialog?.dismiss()
        }

        chooseButton.setOnClickListener {
            onCashierSelectedListener.invoke(
                SalePointCashier(
                    name = cashierNameEditText.text.toString(),
                    phone = cashierPhoneEditText.text.toString()
                )
            )
            dialog?.dismiss()
        }
    }

    fun setCashierSelectedListener(onCashierSelectedListener: (SalePointCashier) -> Unit) {
        this.onCashierSelectedListener = onCashierSelectedListener
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }
}