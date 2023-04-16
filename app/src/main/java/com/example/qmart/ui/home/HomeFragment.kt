package com.example.qmart.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_LEGACY
import androidx.core.text.HtmlCompat.fromHtml
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.qmart.R
import com.example.qmart.databinding.FragmentHomeBinding
import com.example.qmart.ui.product.ProductViewModel

class HomeFragment : Fragment() {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setUI()

    }

    private fun setToolbar() {
        val firstString = "<font color=${
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        }>Магазин на </font>"

        val secondString = "<font color=${
            ContextCompat.getColor(
                requireContext(),
                R.color.orange_700
            )
        }> Qmart</font>"


        binding.toolbarTextView.text = fromHtml(
            firstString + secondString,
            FROM_HTML_MODE_LEGACY
        )
    }

    private fun setUI() = with(binding) {
        inSaleButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_home_to_navigation_products)
        }
        viewModel.productsLiveData.observe(viewLifecycleOwner) {
            inSaleCountTextView.text = it.size.toString()
        }
    }

}