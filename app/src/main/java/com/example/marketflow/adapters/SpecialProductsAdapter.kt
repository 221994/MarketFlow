package com.example.marketflow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.marketflow.data.Product
import com.example.marketflow.databinding.SpecialRvItemBinding

class SpecialProductsAdapter :
    RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {
    private val diffUtilCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtilCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        val view = SpecialRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecialProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val productPosition = differ.currentList[position]
        holder.bind(productPosition)
        holder.itemView.setOnClickListener {
            onClick?.invoke(productPosition)
        }
    }

    inner class SpecialProductsViewHolder(private val binding: SpecialRvItemBinding) :
        ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images?.get(0)).into(ivSpecialItemRv)
                tvProductNameSpecialRv.text = product.name
                tvProductPriceSpecialRv.text = product.price.toString()
            }
        }
    }

    var onClick: ((Product) -> Unit)? = null

}