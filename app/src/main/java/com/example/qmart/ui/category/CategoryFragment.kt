package com.example.qmart.ui.category

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qmart.R
import com.example.qmart.data.Repository.categories
import com.example.qmart.databinding.FragmentCategoryBinding
import com.example.qmart.openFragment
import com.example.qmart.ui.bottomsheet.CategoryBottomSheetFragment
import com.example.qmart.ui.bottomsheet.EMPTY
import com.example.qmart.ui.result.ResultFragment

class CategoryFragment : Fragment() {
    private var selectedIndex = -1
    private var selectedCategory = EMPTY

    companion object {
        fun newInstance() = CategoryFragment()
    }

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var viewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(CategoryViewModel::class.java)
        // TODO: Use the ViewModel

        binding = FragmentCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUI()
    }

    private fun setUI() = with(binding) {
        updateCategoryView()
        chooseCategoryTextView.setOnClickListener {
            val fragment = CategoryBottomSheetFragment().apply {
                setCategorySelectedListener {
                    selectedIndex = it.first
                    selectedCategory = it.second
                    updateCategoryView()
                }
                setCategories(
                    selectedIndex,
                    categories
                )
            }
            fragment.show(parentFragmentManager, "Dialog")
        }

        continueButton.setOnClickListener {
            val result = !binding.chooseCategoryTextView.text.equals(resources.getString(R.string.category))
            openFragment(parentFragmentManager, ResultFragment.newInstance(result), "CategoryFragment")
        }
    }

    private fun updateCategoryView() {
        binding.chooseCategoryTextView.text = if (selectedCategory != EMPTY) selectedCategory else resources.getString(R.string.category)
    }



}