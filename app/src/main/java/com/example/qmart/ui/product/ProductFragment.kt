package com.example.qmart.ui.product

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.qmart.R
import com.example.qmart.data.Product
import com.example.qmart.data.Repository
import com.example.qmart.databinding.FragmentProductBinding
import com.example.qmart.ui.addproduct.ProductCreateViewModel

class ProductFragment : Fragment(), ProductClickListener {

    companion object {
        fun newInstance() = ProductFragment()
    }

    private lateinit var binding: FragmentProductBinding
    private lateinit var products: List<Product>
    private var productAdapter = ProductAdapter(this)
    //private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            products = getParcelableArrayList<Product>(ARG_OBJECT) as List<Product>
            productAdapter.submitList(products)
        }

        setUI()
    }

    fun setUI() = with(binding) {
        //viewModel.setProducts(Repository.products)

        productRecyclerView.apply {
            adapter = productAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                ).apply {
                    setDrawable(ContextCompat.getDrawable(context, R.drawable.vertical_divider)!!)
                })
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                productAdapter.submitList(products.filter {
                    it.name.lowercase().contains(s.toString().lowercase())
                })
            }

        })

        addButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_navigation_plus)
        }

    }

    override fun onClicked(product: Product) {
        val bundle = bundleOf("product" to product)
        findNavController().navigate(R.id.action_productMainFragment_to_productInfoFragment, bundle)
    }

}