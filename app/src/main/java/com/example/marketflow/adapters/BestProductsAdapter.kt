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
import com.example.marketflow.databinding.BestProductRvItemBinding

class BestProductsAdapter : RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>() {

    inner class BestProductsViewHolder(private val binding: BestProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {

            binding.apply {
                tvNameBestProductsRv.text = product.name
                Glide.with(itemView).load(product.images?.get(0)).into(ivBestProductsRv)
                // Debugging: Print the original price
                Log.d("TAG", "Original price is ${product.price}")

                if (product.offerPercentage != null) {
                    val offerFraction =
                        product.offerPercentage / 100 // Convert percentage to fraction
                    val remainingPricePercentage = 1f - offerFraction
                    val priceAfterOffer = remainingPricePercentage * product.price
                    tvNewPriceBestProductsRv.text = "$ ${String.format("%.2f", priceAfterOffer)}"

                    // Debugging: Print the offer percentage, fraction, and calculated discounted price
                    Log.d("TAG", "Offer percentage: ${product.offerPercentage}")
                    Log.d("TAG", "Offer fraction: $offerFraction")
                    Log.d("TAG", "Discounted price: $priceAfterOffer")

                    tvOldPriceBestProductsRv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    tvOldPriceBestProductsRv.text = "$ ${String.format("%.2f", product.price)}"
                } else {
                    tvNewPriceBestProductsRv.visibility = View.INVISIBLE
                    tvOldPriceBestProductsRv.text = "$ ${String.format("%.2f", product.price)}"
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        val view =
            BestProductRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BestProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val currentPosition = differ.currentList[position]
        holder.bind(currentPosition)
        holder.itemView.setOnClickListener {
            onClick?.invoke(currentPosition)
        }
    }

    var onClick: ((Product) -> Unit)? = null

}