package com.example.qmart.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.qmart.R
import com.example.qmart.data.Product
import com.example.qmart.data.ProductType
import com.example.qmart.databinding.FragmentProductMainBinding
import com.example.qmart.ui.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ProductMainFragment : BaseFragment(R.layout.fragment_product_main) {

    companion object {
        fun newInstance() = ProductMainFragment()
    }

    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }
    private lateinit var binding: FragmentProductMainBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var viewModel: ProductViewModel


    private val listOnSale: ArrayList<Product> = ArrayList()

    private val listSold: ArrayList<Product> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        viewModel = ViewModelProvider(baseActivity!!).get(ProductViewModel::class.java)
        binding = FragmentProductMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProductsOnSale()
        getProductsSold()
        setToolbar()
        setUI()
    }

    fun setUI() = with(binding) {
        //viewModel.setProducts(Repository.products)
        closeButton.setOnClickListener {
            baseActivity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun setToolbar() {
        baseActivity?.apply {
            setActionBar(binding.toolbar)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun getProductsOnSale() {
        val result = ArrayList<Product>()
        val categories = ArrayList<String>()
        database.child("CATEGORIES").get().addOnSuccessListener {
            it.children.forEach {
                categories.add(it.key.toString())
            }
            database.get().addOnSuccessListener {
                it.children.forEach {
                    if (categories.contains(it.key.toString())) {
                        it.children.forEach {
                            it.getValue(Product::class.java).also {
                                if (it?.status == Product.ACTIVE) {
                                    result.add(it)
                                }
                            }
                        }
                    }
                }
                listOnSale.addAll(result)
            }
        }
    }

    private fun getProductsSold() {
        val result = ArrayList<Product>()
        val categories = ArrayList<String>()
        database.child("CATEGORIES").get().addOnSuccessListener {
            it.children.forEach {
                categories.add(it.key.toString())
            }
            database.get().addOnSuccessListener {
                it.children.forEach {
                    if (categories.contains(it.key.toString())) {
                        it.children.forEach {
                            it.getValue(Product::class.java).also {
                                if (it?.status == Product.INACTIVE) {
                                    result.add(it)
                                }
                            }
                        }
                    }
                }
                listSold.addAll(result)
                initAdapter()
            }
        }
    }

    private fun initAdapter() {
        viewPagerAdapter = ViewPagerAdapter(this@ProductMainFragment)
        viewPagerAdapter.submitLists(listOnSale, listSold)
        binding.productViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.productTabLayout, binding.productViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> String.format("%s (%d)", ProductType.ONSALE.title, listOnSale.size)
                1 -> String.format("%s (%d)", ProductType.SOLD.title, listSold.size)
                else -> ""
            }
        }.attach()
    }

    override fun onPause() {
        super.onPause()
        listSold.clear()
        listOnSale.clear()
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
        notifyDataSetChanged()
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
