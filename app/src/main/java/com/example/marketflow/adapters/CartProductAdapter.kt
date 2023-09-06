package com.example.marketflow.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.marketflow.data.CartProduct
import com.example.marketflow.databinding.CartProductItemBinding

class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.CartProductViewHolder>() {


    private val diffUtilCallBack = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == oldItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtilCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        val view =
            CartProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartProductPosition = differ.currentList[position]
        holder.bind(cartProductPosition)
        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartProductPosition)
        }
        holder.binding.ivIncreaseQuantity.setOnClickListener {
            onPlusClick?.invoke(cartProductPosition)
        }
        holder.binding.ivDecreaseQuantity.setOnClickListener {
            onMinusClick?.invoke(cartProductPosition)
        }

    }

    inner class CartProductViewHolder(val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images?.get(0)).into(imageCartProduct)
                tvProductCartName.text = cartProduct.product.name
                tvCartProductQuantity.text = cartProduct.quantity.toString()
                imageCartProductColor.setImageDrawable(
                    ColorDrawable(
                        cartProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )
                tvCartProductSize.text = cartProduct.selectedSize ?: "".also {
                    imageCartProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT))
                }

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

    var onProductClick: ((CartProduct) -> Unit)? = null
    var onPlusClick: ((CartProduct) -> Unit)? = null
    var onMinusClick: ((CartProduct) -> Unit)? = null

}