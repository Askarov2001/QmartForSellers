package com.example.qmart.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qmart.databinding.ItemCharacteristicBinding

class CharacteristicAdapter : ListAdapter<Pair<String, String>, CharacteristicViewHolder>(CharacteristicDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacteristicViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCharacteristicBinding.inflate(inflater, parent, false)

        return CharacteristicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacteristicViewHolder, position: Int) {
        val characteristic = getItem(position)

        with(holder.binding) {
            keyTextView.text = characteristic.first
            valueTextView.text = characteristic.second
        }
    }
}

class CharacteristicViewHolder(val binding: ItemCharacteristicBinding) : RecyclerView.ViewHolder(binding.root)

class CharacteristicDiffUtil : DiffUtil.ItemCallback<Pair<String,String>>() {
    override fun areItemsTheSame(
        oldItem: Pair<String, String>,
        newItem: Pair<String, String>
    ): Boolean {
        return oldItem.second == newItem.second
    }

    override fun areContentsTheSame(
        oldItem: Pair<String, String>,
        newItem: Pair<String, String>
    ): Boolean {
        return oldItem == newItem
    }
}