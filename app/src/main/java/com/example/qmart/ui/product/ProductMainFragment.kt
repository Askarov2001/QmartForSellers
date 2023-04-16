package com.example.qmart.ui.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.qmart.data.Product
import com.example.qmart.data.ProductType
import com.example.qmart.data.Repository
import com.example.qmart.databinding.FragmentProductMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class ProductMainFragment : Fragment() {

    companion object {
        fun newInstance() = ProductMainFragment()
    }

    private lateinit var binding: FragmentProductMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewModel: ProductViewModel


    private var listOnSale: List<Product> = emptyList()
    private var listSold: List<Product> = emptyList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        viewModel = ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)
        binding = FragmentProductMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPagerAdapter = ViewPagerAdapter(this@ProductMainFragment)

        listOnSale = Repository.products
        listSold = emptyList<Product>()

        setToolbar()
        setUI()
    }

    fun setUI() = with(binding) {
        //viewModel.setProducts(Repository.products)

        viewModel.productsLiveData.observe(viewLifecycleOwner) {
            listOnSale = it
        }

        viewPagerAdapter.submitLists(
            listOnSale,
            listSold
        )

        productViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(productTabLayout, productViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> String.format("%s (%d)", ProductType.ONSALE.title, listOnSale.size)
                1 -> String.format("%s (%d)", ProductType.SOLD.title, listSold.size)
                else -> ""
            }
        }.attach()

        closeButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setToolbar(){
        requireActivity().apply {
            setActionBar(binding.toolbar)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}

const val ARG_OBJECT = "object"

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    private var listOnSale: List<Product> = emptyList()
    private var listSold: List<Product> = emptyList()

    fun submitLists(listOnSale: List<Product>, listSold: List<Product>) {
        this.listOnSale = listOnSale
        this.listSold = listSold
    }

    override fun createFragment(position: Int): Fragment {

        val fragment = ProductFragment()

        if (position == 0) {
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(ARG_OBJECT, ArrayList(listOnSale))
            }
        } else {
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(ARG_OBJECT, ArrayList(listSold))
            }
        }

        return fragment
    }
}
