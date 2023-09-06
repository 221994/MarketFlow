package com.example.marketflow.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.marketflow.databinding.ColorRvItemBinding

class ColorsAdapter : RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder>() {
    var selectedPosition = -1

    inner class ColorsViewHolder(val binding: ColorRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Int, position: Int) {
            val imageColorPicker = ColorDrawable(color)
            binding.ivColor.setImageDrawable(imageColorPicker)
            if (position == selectedPosition) {
                // That means the colors is selected
                binding.apply {
                    ivShadowColor.visibility = View.VISIBLE
                    ivPickColorIcon.visibility = View.VISIBLE
                }
            } else {
                // That means the colors is not selected
                binding.apply {
                    ivShadowColor.visibility = View.INVISIBLE
                    ivPickColorIcon.visibility = View.INVISIBLE
                }
            }
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem

        }
    }
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder {
        val view = ColorRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color, position)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) {
                val previouslySelectedPosition = selectedPosition
                selectedPosition = holder.adapterPosition
                notifyItemChanged(previouslySelectedPosition) // Unselect the previously selected color
                notifyItemChanged(selectedPosition) // Select the newly clicked color
                onItemClick?.invoke(color)
            } else {
                selectedPosition = holder.adapterPosition
                notifyItemChanged(selectedPosition) // Select the newly clicked color
                onItemClick?.invoke(color)
            }
        }
    }

    var onItemClick: ((Int) -> Unit)? = null
}