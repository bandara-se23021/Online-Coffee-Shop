package com.example.project239.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project239.R
import com.example.project239.databinding.ViewholderPopularBinding
import com.example.project239.domain.ItemsModel

class PopularAdapter(val items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    private lateinit var context: Context

    class ViewHolder(val binding: ViewholderPopularBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        val binding = ViewholderPopularBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleTxt.text = item.title
        holder.binding.priceTxt.text = "$" + item.price.toString()
        holder.binding.subtitleTxt.text = item.extra

        val defaultImageRes = when (position % 4) {
            0 -> R.drawable.pop_2
            1 -> R.drawable.pop_5
            2 -> R.drawable.pop_3
            3 -> R.drawable.pop_4
            4 -> R.drawable.pop_1
            else -> R.drawable.pop_4
        }

        var loaded = false
        if (item.picUrl.isNotEmpty()) {
            val picPath = item.picUrl[0]
            if (picPath.startsWith("http://") || picPath.startsWith("https://")) {
                Glide.with(context)
                    .load(picPath)
                    .placeholder(defaultImageRes)
                    .error(defaultImageRes)
                    .into(holder.binding.pic)
                loaded = true
            } else {
                val cleanName = picPath.replace(".png", "").lowercase()
                var drawableId = context.resources.getIdentifier(
                    cleanName,
                    "drawable",
                    context.packageName
                )
                if (drawableId == 0) {
                    drawableId = context.resources.getIdentifier(
                        "pop_$cleanName",
                        "drawable",
                        context.packageName
                    )
                }
                if (drawableId == 0) {
                    drawableId = context.resources.getIdentifier(
                        "btn_$cleanName",
                        "drawable",
                        context.packageName
                    )
                }

                if (drawableId != 0) {
                    holder.binding.pic.setImageResource(drawableId)
                    loaded = true
                }
            }
        }

        if (!loaded) {
            holder.binding.pic.setImageResource(defaultImageRes)
        }

        holder.itemView.setOnClickListener {
        }
    }

    override fun getItemCount(): Int = items.size
}
