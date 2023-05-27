package com.example.qmart.ui.product

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.example.qmart.R
import com.example.qmart.data.Product
import com.example.qmart.databinding.FragmentProductInfoDetailBinding
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProductInfoDetailFragment : Fragment() {

    companion object {
        fun newInstance() = ProductInfoDetailFragment()
    }

    private lateinit var viewModel: ProductViewModel
    private lateinit var binding: FragmentProductInfoDetailBinding
    private lateinit var product: Product
    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)
        binding = FragmentProductInfoDetailBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(PRODUCTARG) }?.apply {
            product = getParcelable<Product>(PRODUCTARG) as Product
        }

        setLowCostView()
        setUI()
    }

    fun setLowCostView() {
        val firstString = "<font color=${
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        }>Самая низкая цена </font>"

        val secondString = "<font color=${
            ContextCompat.getColor(
                requireContext(),
                R.color.orange_700
            )
        }>" + product.cost + "</font>"


        binding.lowCostTextView.text = HtmlCompat.fromHtml(
            firstString + secondString,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    fun setUI() = with(binding) {
        setToolbar()
        title.text = product.name
        productTabLayout.addTab(productTabLayout.newTab().setText(R.string.info), true)
        productCategoryTextView.text = getString(product.category.nameRes)
        productNameTextView.text = product.name
        productCostEditText.setText(product.cost.toInt().toString())
        productDescriptionTextView.text = product.description
        saveChangesButton.setOnClickListener {
            product.cost = productCostEditText.text.toString().toInt()
            viewModel.updateProductCost(product)
            setLowCostView()
            val map = HashMap<String, Any>()
            map["cost"] = productCostEditText.text.toString().toInt()
            database.child(product.category.name.uppercase()).child(product.id)
                .updateChildren(map as Map<String, Any>)

        }
        closeButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setToolbar() {
        requireActivity().apply {
            setActionBar(binding.toolbar)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

}