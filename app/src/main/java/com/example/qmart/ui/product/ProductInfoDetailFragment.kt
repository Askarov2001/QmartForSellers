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

class ProductInfoDetailFragment : Fragment() {

    companion object {
        fun newInstance() = ProductInfoDetailFragment()
    }

    private lateinit var viewModel: ProductViewModel
    private lateinit var binding: FragmentProductInfoDetailBinding
    private lateinit var product: Product

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
        }>"+product.cost+"</font>"


        binding.lowCostTextView.text = HtmlCompat.fromHtml(
            firstString + secondString,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
    }

    fun setUI() = with(binding) {
        productImageView.setImageURI(product.productImage1)
        productCategoryTextView.text = product.category
        productNameTextView.text = product.name
        productCostEditText.setText(product.cost.toInt().toString())
        productDescriptionTextView.text = product.description
        saveChangesButton.setOnClickListener {
            product.cost = productCostEditText.text.toString().toInt()
            viewModel.updateProductCost(product)
            setLowCostView()
        }
    }

}