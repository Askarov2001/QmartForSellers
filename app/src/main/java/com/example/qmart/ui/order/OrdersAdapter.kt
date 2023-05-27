package com.example.qmart.ui.order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qmart.R
import com.example.qmart.databinding.ItemOrdersBinding
import kotlinx.android.extensions.LayoutContainer

class OrdersAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listener: OrdersAdapterListener? = null
    var list = ArrayList<Order>()
        set(value) {
            notifyDataSetChanged()
            field = value
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MenuItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_orders, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is MenuItemViewHolder) {
            holder.bind(item, position + 1, listener)
        }
    }

    class MenuItemViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        private val binding: ItemOrdersBinding = ItemOrdersBinding.bind(containerView)

        fun bind(item: Order, position: Int, listener: OrdersAdapterListener?) {
            binding.title.text = "Заказ #${position}"
            binding.root.setOnClickListener {
                listener?.onClick(item.id)
            }
        }
    }
}

interface OrdersAdapterListener {
    fun onClick(id: String?)
}
