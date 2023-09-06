package com.example.marketflow.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.marketflow.R
import com.example.marketflow.data.Address
import com.example.marketflow.databinding.AddressRvItemBinding

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(val binding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address, isSelected: Boolean) {
            binding.apply {
                buttonAddress.text = address.addressTitle
                if (isSelected) {
                    buttonAddress.background =
                        ContextCompat.getDrawable(itemView.context, R.color.g_blue)
                    Log.d("Clicked", "Button Clicked ")
                } else {
                    buttonAddress.background =
                        ContextCompat.getDrawable(itemView.context,R.color.g_white)
                    Log.d("Clicked", "Button Not Clicked ")

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val view = AddressRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var selectedAddress = -1

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = differ.currentList[position]
        holder.bind(address, selectedAddress == position)
        holder.binding.buttonAddress.setOnClickListener {
            notifyItemChanged(selectedAddress) // Unselect the previously selected item
            selectedAddress = holder.adapterPosition // Update selectedAddress
            notifyItemChanged(selectedAddress) // Select the newly clicked item
            onClick?.invoke(address)
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle && oldItem.fullName == newItem.fullName
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)
    var onClick: ((Address) -> Unit)? = null
}