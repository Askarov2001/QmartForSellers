package com.example.qmart.ui.bottomsheet

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.example.qmart.R
import com.example.qmart.data.CategoryArgument
import com.example.qmart.databinding.FragmentCategoryBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategoryBottomSheetFragment : BottomSheetDialogFragment() {

    private var selectedIndex = -1
    private var onCategorySelectedListener: (Pair<Int, String>) -> Unit = {}
    private var categories: List<String> = emptyList()

    lateinit var binding: FragmentCategoryBottomSheetBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        binding = FragmentCategoryBottomSheetBinding.inflate(layoutInflater)
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

        val params = view.layoutParams
        params.height = Resources.getSystem().displayMetrics.heightPixels
        view.layoutParams = params
    }

    private fun setUI() = with(binding) {
        val categoriesAdapter = CategoryAdapter()

        backButtonIV.setOnClickListener {
            dialog?.dismiss()
        }

        recyclerView.apply {
            adapter = categoriesAdapter
        }

        resetTextView.setOnClickListener {
            categoriesAdapter.reset()
        }

        chooseButton.setOnClickListener {

            onCategorySelectedListener.invoke(
                Pair(
                    categoriesAdapter.getSelectedIndex(),
                    categoriesAdapter.getSelectedName()
                )
            )

            dialog?.dismiss()
        }

        categoriesAdapter.apply {
            setSelectedIndex(selectedIndex)
            submitList(categories)
        }
    }

    fun setCategories(index: Int, list: List<String>) {
        selectedIndex = index
        categories = list
    }

    fun setCategorySelectedListener(onCategorySelectedListener: (Pair<Int, String>) -> Unit) {
        this.onCategorySelectedListener = onCategorySelectedListener
    }


    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }
}