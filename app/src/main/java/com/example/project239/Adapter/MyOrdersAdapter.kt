
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
import android.app.AlertDialog
import android.widget.Toast
import com.example.project239.Helper.ManagmentOrders

class MyOrdersAdapter(
    private val orders: ArrayList<OrderModel>,
    private val onOrdersChanged: () -> Unit
) : RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>() {


    private lateinit var ordersManager: ManagmentOrders
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


        holder.binding.orderIdTxt.text =
            "Order #${order.orderId}"


        val formatter = SimpleDateFormat(
            "dd MMM yyyy, hh:mm a",
            Locale.getDefault()
        )

        holder.binding.dateTxt.text =
            formatter.format(Date(order.orderDate))


        holder.binding.itemsContainer.removeAllViews()


        for (item in order.items) {

            val itemBinding = ViewholderOrderItemBinding.inflate(
                LayoutInflater.from(context),
                holder.binding.itemsContainer,
                false
            )


            itemBinding.itemNameTxt.text =
                "${item.title} x ${item.numberInCart}"


            val itemTotal = item.price * item.numberInCart

            itemBinding.itemPriceTxt.text =
                "$%.2f".format(Locale.US, itemTotal)


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


            holder.binding.itemsContainer.addView(
                itemBinding.root
            )
        }


        holder.binding.totalTxt.text =
            "Total: $%.2f".format(
                Locale.US,
                order.totalPrice
            )


        holder.binding.statusTxt.text =
            order.status


        holder.binding.deleteBtn.setOnClickListener {

            AlertDialog.Builder(context)
                .setTitle("Delete Order")
                .setMessage(
                    "Are you sure you want to delete this order from history?"
                )
                .setPositiveButton("Delete") { _, _ ->

                    ordersManager = ManagmentOrders(context)

                    // Delete from local storage
                    ordersManager.deleteOrder(order.orderId)

                    // Find correct position
                    val currentPosition =
                        orders.indexOfFirst {
                            it.orderId == order.orderId
                        }

                    if (currentPosition != -1) {

                        orders.removeAt(currentPosition)

                        notifyItemRemoved(currentPosition)

                        onOrdersChanged()
                    }

                    Toast.makeText(
                        context,
                        "Order Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

    }

    override fun getItemCount(): Int = orders.size
}
