package com.example.qmart.ui.addproduct

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.qmart.R

class GalleryAdapter() : ListAdapter<Uri, UriViewHolder>(UriDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UriViewHolder {
        return UriViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UriViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}


class UriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView = itemView.findViewById<ImageView>(R.id.imageView)

    fun bind(image: Uri) {
        imageView.setImageURI(image)
    }
}

class UriDiffUtil : DiffUtil.ItemCallback<Uri>() {
    override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean =
        oldItem == newItem


    override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean =
        oldItem == newItem

}
