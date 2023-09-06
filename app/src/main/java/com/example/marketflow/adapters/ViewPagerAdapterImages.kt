package com.example.marketflow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketflow.databinding.Viewpager2ImageItemBinding

class ViewPagerAdapterImages : RecyclerView.Adapter<ViewPagerAdapterImages.ViewPager2ViewHolder>() {
    inner class ViewPager2ViewHolder(val binding: Viewpager2ImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String) {
            Glide.with(itemView).load(image).into(binding.ivProductDetails)
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPager2ViewHolder {
        val view =
            Viewpager2ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPager2ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewPager2ViewHolder, position: Int) {
        val imagePosition = differ.currentList[position]
        holder.bind(imagePosition)
    }
}