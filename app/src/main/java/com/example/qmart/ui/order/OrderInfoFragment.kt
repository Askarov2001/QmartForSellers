package com.example.qmart.ui.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.qmart.R
import com.example.qmart.data.Product
import com.example.qmart.databinding.FragmentOrderInfoBinding
import com.example.qmart.ui.product.ProductAdapter
import com.example.qmart.ui.product.ProductClickListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class OrderInfoFragment : Fragment(R.layout.fragment_order_info), ProductClickListener {
    private val binding: FragmentOrderInfoBinding by viewBinding()
    private val database: DatabaseReference by lazy {
        Firebase.database.reference.child("ORDERS").child(orderId)
    }

    private val orderId: String by lazy {
        arguments?.getString("orderId") ?: ""
    }

    private val title: String by lazy {
        arguments?.getString("title") ?: ""
    }

    private val adapter by lazy {
        ProductAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            productsList.adapter = adapter
            val products = arrayListOf<Product>()
            title.text = this@OrderInfoFragment.title
            database.child("address").get().addOnSuccessListener {
                address.text = it.value.toString()
            }
            database.child("clientName").get().addOnSuccessListener {
                name.text = it.value.toString()
            }
            database.child("phone").get().addOnSuccessListener {
                phone.text = it.value.toString()
            }

            database.child("totalCost").get().addOnSuccessListener {
                sum.text = it.value.toString()
            }
            database.child("products").get().addOnSuccessListener {
                it.children.forEach {
                    val order = it.getValue(OrderProduct::class.java)
                    products.add(
                        Product(
                            name = order?.name ?: "",
                            images = order?.image ?: "",
                            cost = order?.count ?: 0,
                        )
                    )
                }
                adapter.submitList(products)
            }
        }

    }

    override fun onClicked(product: Product) {

    }
}