package com.example.project239.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project239.domain.CategoryModel
import com.example.project239.R
import com.example.project239.databinding.ViewholderCategoryBinding

class CategoryAdapter(
    val items: MutableList<CategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var selectedPosition = 0
    private var lastSelectedPosition = -1

    inner class ViewHolder(
        val binding: ViewholderCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.binding.titleCat.text = item.title

        holder.binding.root.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition != RecyclerView.NO_POSITION && selectedPosition != currentPosition) {
                lastSelectedPosition = selectedPosition
                selectedPosition = currentPosition
                if (lastSelectedPosition != -1) {
                    notifyItemChanged(lastSelectedPosition)
                }
                notifyItemChanged(selectedPosition)
            }
        }

        if (selectedPosition == position) {
            holder.binding.titleCat.setBackgroundResource(R.drawable.brown_full_corner_bg)
            holder.binding.titleCat.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.binding.titleCat.setBackgroundResource(R.drawable.white_full_corner_bg)
            holder.binding.titleCat.setTextColor(ContextCompat.getColor(context, R.color.darkBrown))
        }
    }

    override fun getItemCount(): Int = items.size
}
