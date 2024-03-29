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
import com.example.qmart.MainActivity
import com.example.qmart.R
import com.example.qmart.SharedPref
import com.example.qmart.databinding.FragmentHomeBinding
import com.example.qmart.ui.BaseFragment
import com.example.qmart.ui.order.Order
import com.example.qmart.ui.product.ProductViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeFragment : BaseFragment(R.layout.fragment_home) {
    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: ProductViewModel

    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(baseActivity!!).get(ProductViewModel::class.java)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBoughtSum()
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
        val addedCategories = ArrayList<String>()
        var count = 0
        database.child("CATEGORIES").get().addOnSuccessListener {
            it.children.forEach {
                addedCategories.add(it.key.toString())
            }

            addedCategories.forEach {
                database.child(it.toString().uppercase()).get().addOnSuccessListener {
                    it.children.forEach {
                        if (it.child("status").value.toString() == "ACTIVE") {
                            count++
                        }
                    }
                    binding.inSaleCountTextView.text = count.toString()
                }
            }
        }
    }

    private fun getBoughtSum() {
        var total :Long= 0
        val orderIds = ArrayList<String>()
        database.child("ORDERS").get().addOnSuccessListener {
            it.children.forEach {
                orderIds.add(it.key ?: "")
            }
            orderIds.forEach { ids ->
                database.child("ORDERS").child(ids).child("sellers").get()
                    .addOnSuccessListener { shapshot ->
                            val uid = baseActivity?.getValue(SharedPref.UID)
                            uid?.let {
                                if (shapshot.value.toString().contains(uid)) {
                                    database.child("ORDERS").child(ids).child("isTaken").get()
                                        .addOnSuccessListener {
                                            if (it.value == true) {
                                                database.child("ORDERS").child(ids)
                                                    .child("totalCost").get()
                                                    .addOnSuccessListener {
                                                        total += (it.value as Long)
                                                        binding.boughtSum.text = "${total} t"
                                                    }
                                            }
                                        }
                                }

                            }
                        }
                    }
        }

    }

}