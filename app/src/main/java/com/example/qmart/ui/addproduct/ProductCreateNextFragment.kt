package com.example.qmart.ui.addproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.qmart.R
import com.example.qmart.addTextListener
import com.example.qmart.data.Location
import com.example.qmart.data.Repository
import com.example.qmart.databinding.FragmentProductCreateNextBinding
import com.example.qmart.ui.bottomsheet.CountryBottomSheetFragment
import com.example.qmart.ui.bottomsheet.EMPTY

class ProductCreateNextFragment : Fragment() {
    private var selectedIndex = -1
    private var selectedCountry = EMPTY

    companion object {
        fun newInstance() = ProductCreateNextFragment()
    }

    private lateinit var binding: FragmentProductCreateNextBinding

    private lateinit var viewModel: ProductCreateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(ProductCreateViewModel::class.java)

        binding = FragmentProductCreateNextBinding.inflate(layoutInflater)
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
        viewModel.getProduct().apply {
            selectedCountry = this.country
            chooseCountryTextView.text = if (this.country != EMPTY && this.country != "") selectedCountry else resources.getString(R.string.country)
            productExpirationDateEditText.setText(this.expirationDate)
            productPackageTypeEditText.setText(this.packageType)
            productTypeEditText.setText(this.type)
            productIngredientsEditText.setText(this.ingredients)
            productCharacteristicsEditText.setText(this.characteristics)
        }

        chooseCountryTextView.setOnClickListener {
            val fragment = CountryBottomSheetFragment().apply {
                setCategorySelectedListener {
                    selectedIndex = it.first
                    selectedCountry = it.second
                    viewModel.setProductCountry(selectedCountry)
                    updateCountryView()
                }
                setCountries(
                    selectedIndex,
                    Repository.countries,
                    Location.COUNTRY
                )
            }
            fragment.show(parentFragmentManager, "Dialog")
        }

        productExpirationDateEditText.addTextListener {
            viewModel.setProductExpirationDate(it)
        }

        productPackageTypeEditText.addTextListener {
            viewModel.setProductPackageType(it)
        }

        productTypeEditText.addTextListener {
            viewModel.setProductType(it)
        }

        productIngredientsEditText.addTextListener {
            viewModel.setProductIngredients(it)
        }

        productCharacteristicsEditText.addTextListener {
            viewModel.setProductCharacteristics(it)
        }

        closeButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        continueButton.setOnClickListener {
            //openFragment(parentFragmentManager, newInstance(), "ProductCreateNextFragment")
        }
    }

    private fun updateCountryView() {
        binding.chooseCountryTextView.text = if (selectedCountry != EMPTY) selectedCountry else resources.getString(R.string.country)
    }

}