package com.example.project239.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project239.R
import com.example.project239.Helper.ManagmentCart
import com.example.project239.Helper.ManagmentWishlist
import com.example.project239.databinding.ViewholderWishlistBinding
import com.example.project239.domain.ItemsModel

class WishlistAdapter(
    private val items: ArrayList<ItemsModel>,
    private val context: Context,
    private val onWishlistChanged: () -> Unit
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    private val wishlistManager = ManagmentWishlist(context)
    private val cartManager = ManagmentCart(context)

    class ViewHolder(val binding: ViewholderWishlistBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ViewholderWishlistBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.priceTxt.text = "$${item.price}"

        if (item.picUrl.isNotEmpty()) {
            val imagePath = item.picUrl[0]

            if (imagePath.startsWith("http://") ||
                imagePath.startsWith("https://")) {
                Glide.with(context)
                    .load(imagePath)
                    .placeholder(R.drawable.pop_2)
                    .error(R.drawable.pop_2)
                    .into(holder.binding.pic)
            } else {
                val cleanName = imagePath
                    .substringBeforeLast(".")
                    .lowercase()

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
                } else {
                    holder.binding.pic.setImageResource(R.drawable.pop_2)
                }
            }
        } else {
            holder.binding.pic.setImageResource(R.drawable.pop_2)
        }

        holder.binding.removeBtn.setOnClickListener {
            wishlistManager.removeItem(item)

            val currentPosition = holder.bindingAdapterPosition

            if (currentPosition != RecyclerView.NO_POSITION) {
                items.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
                onWishlistChanged()
            }
        }

        holder.binding.addCartBtn.setOnClickListener {
            val cartItem = item.copy(
                numberInCart = 1
            )

            cartManager.insertItems(cartItem)

            Toast.makeText(
                context,
                "Added to Cart",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int = items.size
}
