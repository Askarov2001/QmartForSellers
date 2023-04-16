package com.example.qmart.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.qmart.databinding.FragmentMainBinding
import com.example.qmart.openFragment
import com.example.qmart.ui.category.CategoryFragment
import com.example.qmart.ui.addproduct.ProductCreateFragment
import com.example.qmart.ui.salepoint.SalePointFragment

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    private fun setUI() = with(binding){
        categoryPageButton.setOnClickListener {
            openFragment(parentFragmentManager, CategoryFragment.newInstance(), "MainFragment")
        }
        createProductPageButton.setOnClickListener {
            openFragment(parentFragmentManager, ProductCreateFragment.newInstance(), "MainFragment")
        }
        addSalePointPageButton.setOnClickListener {
            openFragment(parentFragmentManager, SalePointFragment.newInstance(), "MainFragment")
        }
    }

}