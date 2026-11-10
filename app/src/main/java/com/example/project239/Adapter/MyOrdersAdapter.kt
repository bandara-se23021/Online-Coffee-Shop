
package com.example.project239.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project239.R
import com.example.project239.databinding.ViewholderMyOrderBinding
import com.example.project239.databinding.ViewholderOrderItemBinding
import com.example.project239.domain.OrderModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyOrdersAdapter(
    private val orders: ArrayList<OrderModel>
) : RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: ViewholderMyOrderBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val binding = ViewholderMyOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        val order = orders[position]
        val context = holder.itemView.context

        // Order ID
        holder.binding.orderIdTxt.text =
            "Order #${order.orderId}"

        // Order Date
        val formatter = SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        )

        holder.binding.dateTxt.text =
            formatter.format(Date(order.orderDate))

        // Clear old items (RecyclerView recycling)
        holder.binding.itemsContainer.removeAllViews()

        // Add each coffee item separately
        for (item in order.items) {

            val itemBinding = ViewholderOrderItemBinding.inflate(
                LayoutInflater.from(context),
                holder.binding.itemsContainer,
                false
            )

            // Coffee name and quantity
            itemBinding.itemNameTxt.text =
                "${item.title} x ${item.numberInCart}"

            // Item subtotal
            val itemTotal = item.price * item.numberInCart

            itemBinding.itemPriceTxt.text =
                "$%.2f".format(Locale.US, itemTotal)

            // Coffee image
            val imagePath = item.picUrl.firstOrNull()

            if (!imagePath.isNullOrEmpty()) {

                if (
                    imagePath.startsWith("http://") ||
                    imagePath.startsWith("https://")
                ) {

                    Glide.with(holder.itemView)
                        .load(imagePath)
                        .placeholder(R.drawable.pop_2)
                        .error(R.drawable.pop_2)
                        .into(itemBinding.coffeeImg)

                } else {

                    val cleanName = imagePath
                        .substringBeforeLast(".")
                        .lowercase(Locale.ROOT)

                    val drawableId =
                        context.resources.getIdentifier(
                            cleanName,
                            "drawable",
                            context.packageName
                        )

                    if (drawableId != 0) {

                        Glide.with(holder.itemView)
                            .load(drawableId)
                            .into(itemBinding.coffeeImg)

                    } else {

                        itemBinding.coffeeImg.setImageResource(
                            R.drawable.pop_2
                        )
                    }
                }

            } else {

                itemBinding.coffeeImg.setImageResource(
                    R.drawable.pop_2
                )
            }

            // Add item row to white order card
            holder.binding.itemsContainer.addView(
                itemBinding.root
            )
        }

        // Order Total
        holder.binding.totalTxt.text =
            "Total: $%.2f".format(
                Locale.US,
                order.totalPrice
            )

        // Order Status
        holder.binding.statusTxt.text =
            order.status
    }

    override fun getItemCount(): Int = orders.size
}
