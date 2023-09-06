package com.example.marketflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.marketflow.databinding.SizeRvItemBinding

class SizesAdapter : RecyclerView.Adapter<SizesAdapter.SizeViewHolder>() {
    var selectedPosition = -1

    inner class SizeViewHolder(val binding: SizeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(size: String, position: Int) {
            binding.tvSize.text = size

            if (position == selectedPosition) {
                // That means the Size is selected
                binding.apply {
                    ivShadowColor.visibility = View.VISIBLE
                }
            } else {
                // That means the Size is not selected
                binding.apply {
                    ivShadowColor.visibility = View.INVISIBLE
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem

        }
    }
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeViewHolder {
        val view = SizeRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SizeViewHolder, position: Int) {
        val sizePosition = differ.currentList[position]
        holder.bind(sizePosition, position)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) {
                val previouslySelectedPosition = selectedPosition
                selectedPosition = holder.adapterPosition
                notifyItemChanged(previouslySelectedPosition) // Unselect the previously selected color
                notifyItemChanged(selectedPosition) // Select the newly clicked color
                onItemClick?.invoke(sizePosition)
            } else {
                selectedPosition = holder.adapterPosition
                notifyItemChanged(selectedPosition) // Select the newly clicked color
                onItemClick?.invoke(sizePosition)
            }
        }
    }

    var onItemClick: ((String) -> Unit)? = null
}