package com.example.qmart.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qmart.data.OrderType
import com.example.qmart.databinding.ItemOrderTypeBinding
import com.example.qmart.ui.bottomsheet.StringDiffUtil

class OrderTypeAdapter : ListAdapter<Pair<OrderType, Int>, TypeViewHolder>(OrderDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOrderTypeBinding.inflate(inflater, parent, false)

        return TypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = getItem(position)

        with(holder.binding) {
            orderTypeTextView.text = String.format("%s (%d)", type.first.title, type.second)
        }
    }


}

class TypeViewHolder(val binding: ItemOrderTypeBinding) : RecyclerView.ViewHolder(binding.root)



class OrderDiffUtil : DiffUtil.ItemCallback<Pair<OrderType, Int>>() {
    override fun areItemsTheSame(
        oldItem: Pair<OrderType, Int>,
        newItem: Pair<OrderType, Int>
    ): Boolean {
        return oldItem.first.name == newItem.first.name
    }

    override fun areContentsTheSame(
        oldItem: Pair<OrderType, Int>,
        newItem: Pair<OrderType, Int>
    ): Boolean {
        return oldItem == newItem
    }

}