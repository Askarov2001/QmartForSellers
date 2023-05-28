package com.example.qmart.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qmart.R
import com.example.qmart.databinding.FragmentOrderBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class OrderFragment : Fragment() {

    companion object {
        fun newInstance() = OrderFragment()
    }

    private lateinit var binding: FragmentOrderBinding
    private lateinit var viewModel: OrderViewModel
    private var orderAdapter = OrdersAdapter()
    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(layoutInflater)
        //viewModel = ViewModelProvider(this).get(OrderViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        setUI()

    }

    fun setUI() = with(binding) {
        val orders: ArrayList<Order> = ArrayList()
        orderRecyclerView.adapter = orderAdapter
        closeButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        database.child("ORDERS").get().addOnSuccessListener {
            it.children.forEach {
                orders.add(Order(it.key))
            }
            if (orders.size > 0) {
                binding.orderRecyclerView.visibility = View.VISIBLE
                binding.emptyTextView.visibility = View.GONE
            } else {
                binding.orderRecyclerView.visibility = View.GONE
                binding.emptyTextView.visibility = View.VISIBLE
            }
            orderAdapter.list = orders
            orderAdapter.listener = object : OrdersAdapterListener {
                override fun onClick(id: String?, title: String) {
                    val bundle = bundleOf("orderId" to id, "title" to title)
                    findNavController().navigate(R.id.orders_to_order_info, bundle)
                }
            }
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