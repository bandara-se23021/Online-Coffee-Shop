package com.example.project239.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.project239.R
import com.example.project239.Helper.ChangeNumberItemsListener
import com.example.project239.Helper.ManagmentCart
import com.example.project239.databinding.ViewholderCartBinding
import com.example.project239.domain.ItemsModel

class CartAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    context: Context,
    var changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(val binding: ViewholderCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val managmentCart = ManagmentCart(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItemSelected[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "$${item.price}"
        val total = Math.round(item.numberInCart * item.price * 100.0) / 100.0
        holder.binding.totalEachItem.text = "$${total}"
        holder.binding.numberInCartTxt.text = item.numberInCart.toString()

        if (item.picUrl.isNotEmpty()) {
            val picPath = item.picUrl[0]
            if (picPath.startsWith("http://") || picPath.startsWith("https://")) {
                Glide.with(holder.itemView.context)
                    .load(picPath)
                    .transform(CenterCrop())
                    .placeholder(R.drawable.pop_2)
                    .error(R.drawable.pop_2)
                    .into(holder.binding.picCart)
            } else {
                val cleanName = picPath.replace(".png", "").lowercase()
                var drawableId = holder.itemView.context.resources.getIdentifier(
                    cleanName,
                    "drawable",
                    holder.itemView.context.packageName
                )
                if (drawableId == 0) {
                    drawableId = holder.itemView.context.resources.getIdentifier(
                        "pop_$cleanName",
                        "drawable",
                        holder.itemView.context.packageName
                    )
                }
                if (drawableId == 0) {
                    drawableId = holder.itemView.context.resources.getIdentifier(
                        "btn_$cleanName",
                        "drawable",
                        holder.itemView.context.packageName
                    )
                }

                if (drawableId != 0) {
                    holder.binding.picCart.setImageResource(drawableId)
                } else {
                    Glide.with(holder.itemView.context)
                        .load(picPath)
                        .transform(CenterCrop())
                        .placeholder(R.drawable.pop_2)
                        .error(R.drawable.pop_2)
                        .into(holder.binding.picCart)
                }
            }
        } else {
            holder.binding.picCart.setImageResource(R.drawable.pop_2)
        }

        holder.binding.plusBtn.setOnClickListener {
            managmentCart.plusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }

        holder.binding.minusBtn.setOnClickListener {
            managmentCart.minusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }

        holder.binding.removeItemBtn.setOnClickListener {
            managmentCart.removeItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}
