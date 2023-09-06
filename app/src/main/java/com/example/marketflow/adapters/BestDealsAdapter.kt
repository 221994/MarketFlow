package com.example.marketflow.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketflow.data.Product
import com.example.marketflow.databinding.BestDealsRvItemBinding

class BestDealsAdapter : RecyclerView.Adapter<BestDealsAdapter.BestDealsProducts>() {

    inner class BestDealsProducts(private val binding: BestDealsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            binding.apply {
                tvNameBestDealRv.text = product.name
                Glide.with(itemView).load(product.images?.get(0)).into(ivBestDealsRv)
                // Actually i have a problem in the total price items. working on it
                Log.d("TAG", "Original price is ${product.price}")
                if (product.offerPercentage != null) {
                    val offerFraction =
                        product.offerPercentage / 100 // Convert percentage to fraction
                    val remainingPricePercentage = 1f - offerFraction
                    val priceAfterOffer = remainingPricePercentage * product.price
                    tvNewPriceBestDealRv.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                    Log.d("TAG", "Offer percentage: ${product.offerPercentage}")
                    Log.d("TAG", "Offer fraction: $offerFraction")
                    Log.d("TAG", "Discounted price: $priceAfterOffer")
                    tvOldPriceBestDealRv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvOldPriceBestDealRv.text = "$ ${String.format("%.2f", product.price)}"
                } else {
                    tvOldPriceBestDealRv.visibility = View.INVISIBLE
                    tvOldPriceBestDealRv.text = "$ ${String.format("%.2f", product.price)}"
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealsProducts {
        val view =
            BestDealsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestDealsProducts(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestDealsProducts, position: Int) {
        val currentPosition = differ.currentList[position]
        holder.bind(currentPosition)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentPosition)
        }
    }

    var onClick: ((Product) -> Unit)? = null

}