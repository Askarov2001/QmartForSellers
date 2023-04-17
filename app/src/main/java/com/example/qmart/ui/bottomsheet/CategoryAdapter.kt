package com.example.qmart.ui.bottomsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qmart.R
import com.example.qmart.data.Categories

private var checkedPosition = -1
const val EMPTY = "empty"

class CategoryAdapter() : ListAdapter<Categories, CategoryViewHolder>(StringDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position).nameRes)
        holder.itemView.setOnClickListener {
            checkedPosition = holder.adapterPosition
            notifyDataSetChanged()
        }
    }

    fun getSelectedIndex(): Int {
        return checkedPosition
    }

    fun getSelected(): Categories {
        if (checkedPosition == -1) {
            return Categories.PRODUCTS
        }
        return getItem(checkedPosition)
    }

    fun setSelectedIndex(index: Int) {
        checkedPosition = index
        notifyDataSetChanged()
    }

    fun reset() {
        checkedPosition = -1
        notifyDataSetChanged()
    }


}

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val categoryTextView = itemView.findViewById<TextView>(R.id.categoryTextView)

    fun bind(category: Int) {
        categoryTextView.text = itemView.context.getString(category)
        if (checkedPosition == -1) {
            categoryTextView.isSelected = false
        } else {
            categoryTextView.isSelected = checkedPosition == adapterPosition
        }
    }
}

class StringDiffUtil : DiffUtil.ItemCallback<Categories>() {
    override fun areItemsTheSame(oldItem: Categories, newItem: Categories): Boolean {
        return oldItem.nameRes == newItem.nameRes
    }

    override fun areContentsTheSame(oldItem: Categories, newItem: Categories): Boolean {
        return oldItem.nameRes == newItem.nameRes
    }

}


