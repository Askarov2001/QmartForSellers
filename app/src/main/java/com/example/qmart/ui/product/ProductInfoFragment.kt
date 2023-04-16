package com.example.qmart.ui.product

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.qmart.R
import com.example.qmart.data.Product
import com.example.qmart.data.ProductType
import com.example.qmart.databinding.FragmentProductInfoBinding
import com.google.android.material.tabs.TabLayoutMediator

class ProductInfoFragment : Fragment() {

    companion object {
        fun newInstance() = ProductInfoFragment()
    }

    private lateinit var viewPagerAdapter: ProductPagerAdapter
    private lateinit var binding: FragmentProductInfoBinding
    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        product = arguments?.getParcelable("product") ?: Product()

        binding = FragmentProductInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = ProductPagerAdapter(this@ProductInfoFragment)

        setToolbar()
        setUI()
    }

    private fun setToolbar(){
        requireActivity().apply {
            setActionBar(binding.toolbar)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setUI() = with(binding) {
        toolbarTextView.text = product.name

        viewPagerAdapter.submitProduct(product)

        productViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(productTabLayout, productViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> String.format("%s", resources.getString(R.string.info))
                1 -> String.format("%s", resources.getString(R.string.description))
                else -> ""
            }
        }.attach()
    }

}

const val PRODUCTARG = "product"

class ProductPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    private lateinit var product: Product

    fun submitProduct(product: Product) {
        this.product = product
    }

    override fun createFragment(position: Int): Fragment {

        val fragment1 = ProductInfoDetailFragment()
        val fragment2 = ProductDescriptionFragment()

        if(position == 0){
            fragment1.arguments = Bundle().apply {
                putParcelable(PRODUCTARG, product)
            }
            return fragment1
        } else {
            fragment2.arguments = Bundle().apply {
                putParcelable(PRODUCTARG, product)
            }
            return fragment2
        }
    }
}
