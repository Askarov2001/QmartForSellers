package com.example.qmart.ui.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.qmart.MainActivity
import com.example.qmart.SharedPref
import com.example.qmart.databinding.FragmentMainOrderBinding
import com.example.qmart.ui.product.ARG_OBJECT
import com.example.qmart.ui.product.ProductFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class OrderMainFragment : Fragment() {
    private var _binding: FragmentMainOrderBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = OrderMainFragment()
    }

    private val orders: ArrayList<Order> = ArrayList()

    private val taken: ArrayList<Order> = ArrayList()

    private val viewPagerAdapter: ViewPagerAdapter by lazy {
        ViewPagerAdapter(this)
    }
    private val database: DatabaseReference by lazy {
        Firebase.database.reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        _binding = FragmentMainOrderBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getOrders()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getOrders() {
        val orderIds = ArrayList<String>()
        database.child("ORDERS").get().addOnSuccessListener {
            it.children.forEach {
                orderIds.add(it.key ?: "")
            }
            orderIds.forEach { ids ->
                database.child("ORDERS").child(ids).child("sellers").get()
                    .addOnSuccessListener { shapshot ->
                        if (requireActivity() is MainActivity) {
                            val uid = (requireActivity() as MainActivity).getValue(SharedPref.UID)
                            uid?.let {
                                if (shapshot.value.toString().contains(uid)) {
                                    database.child("ORDERS").child(ids).child("isTaken").get()
                                        .addOnSuccessListener {
                                            if (it.value == true) {
                                                taken.add(Order(ids))
                                            } else {
                                                orders.add(Order(ids))
                                            }
                                            initAdapter()
                                        }
                                }

                            }
                        }
                    }
            }
        }
    }

    private fun initAdapter() {
        viewPagerAdapter.submitLists(orders, taken)
        binding.orderRecyclerView.adapter = viewPagerAdapter
        TabLayoutMediator(binding.orderTabLayout, binding.orderRecyclerView) { tab, position ->
            tab.text = when (position) {
                0 -> "Активные${orders.size}"
                1 -> "Принятые${taken.size}"
                else -> ""
            }
        }.attach()
    }
}

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    private var orders: ArrayList<Order> = ArrayList()

    private var taken: ArrayList<Order> = ArrayList()

    fun submitLists(orders: ArrayList<Order>, taken: ArrayList<Order>) {
        this.orders = orders
        this.taken = taken
        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {

        val fragment = OrderFragment()

        if (position == 0) {
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(ARG_OBJECT, ArrayList(orders))
                putBoolean("isTaken", false)
            }
        } else {
            fragment.arguments = Bundle().apply {
                putParcelableArrayList(ARG_OBJECT, ArrayList(taken))
                putBoolean("isTaken", true)
            }
        }

        return fragment
    }
}