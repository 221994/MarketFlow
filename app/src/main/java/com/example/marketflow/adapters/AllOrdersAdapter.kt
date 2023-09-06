package com.example.marketflow.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.marketflow.R
import com.example.marketflow.data.order.Order
import com.example.marketflow.data.order.OrderStatus
import com.example.marketflow.data.order.getOrderStatus
import com.example.marketflow.databinding.OrderItemBinding

class AllOrdersAdapter : RecyclerView.Adapter<AllOrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(orderPosition: Order) {
            binding.apply {
                tvOrderId.text = orderPosition.orderID.toString()
                tvOrderDate.text = orderPosition.date
//                val resources = itemView.resources


                val colorDrawable = when (getOrderStatus(orderPosition.orderStatus)) {

                    is OrderStatus.Ordered -> {
                        ColorDrawable(ContextCompat.getColor(itemView.context,R.color.g_orange_yellow))

                    }

                    is OrderStatus.Confirmed -> {
                        ColorDrawable(ContextCompat.getColor(itemView.context,R.color.g_green))

                    }

                    is OrderStatus.Delivered -> {
                        ColorDrawable(ContextCompat.getColor(itemView.context,R.color.g_green))


                    }

                    is OrderStatus.Canceled -> {
                        ColorDrawable(ContextCompat.getColor(itemView.context,R.color.g_red))


                    }

                    is OrderStatus.Returned -> {
                        ColorDrawable(ContextCompat.getColor(itemView.context,R.color.g_red))


                    }

                }
                imageOrderState.setImageDrawable(colorDrawable)

            }

        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Order>() {

        override fun areItemsTheSame(
            oldItem: Order, newItem: Order
        ): Boolean {
            return oldItem.products == newItem.products
        }

        override fun areContentsTheSame(
            oldItem: Order, newItem: Order
        ): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view = OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val orderPosition = differ.currentList[position]
        holder.bind(orderPosition)
        holder.itemView.setOnClickListener {
            onClick?.invoke(orderPosition)
        }
    }

    var onClick: ((Order) -> Unit)? = null
}