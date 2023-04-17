package com.example.qmart.ui.product

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.qmart.R
import com.example.qmart.data.Product
import com.example.qmart.databinding.FragmentProductDescriptionBinding

class ProductDescriptionFragment : Fragment() {

    companion object {
        fun newInstance() = ProductDescriptionFragment()
    }

    //private lateinit var viewModel: ProductDescriptionViewModel
    private lateinit var binding: FragmentProductDescriptionBinding
    private lateinit var product: Product
    private var characteristicAdapter = CharacteristicAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDescriptionBinding.inflate(layoutInflater)
        //viewModel = ViewModelProvider(this).get(ProductDescriptionViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(PRODUCTARG) }?.apply {
            product = getParcelable<Product>(PRODUCTARG) as Product
        }
        val characteristics = listOf(
            Pair(resources.getString(R.string.characteristics), product.characteristics)
        )

        characteristicAdapter.submitList(characteristics)

        binding.characteristicRecyclerView.apply {
            adapter = characteristicAdapter
        }

    }

}