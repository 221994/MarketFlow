package com.example.marketflow.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketflow.data.CartProduct
import com.example.marketflow.databinding.BillingProductsRvItemBinding

class BillingProductAdapter : RecyclerView.Adapter<BillingProductAdapter.BillingViewHolder>() {

    inner class BillingViewHolder(val binding: BillingProductsRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images?.get(0)).into(imageCartProduct)
                tvProductCartName.text = cartProduct.product.name
                tvBillingProductQuantity.text = cartProduct.quantity.toString()
                cartProduct.product.offerPercentage?.let {
                    if (cartProduct.product.offerPercentage != null) {
                        val offerFraction =
                            cartProduct.product.offerPercentage / 100 // Convert percentage to fraction
                        val remainingPricePercentage = 1f - offerFraction
                        val priceAfterOffer = remainingPricePercentage * cartProduct.product.price
                        tvProductCartPrice.text = "$ ${String.format("%.2f", priceAfterOffer)}"
                    } else {
                        binding.tvProductCartPrice.text = cartProduct.product.price.toString()

                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        val view =
            BillingProductsRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillingViewHolder(view)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val billingProduct = differ.currentList[position]
        holder.bind(billingProduct)
    }
}