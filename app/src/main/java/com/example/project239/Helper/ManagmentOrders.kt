
package com.example.project239.Helper

import android.content.Context
import com.example.project239.domain.OrderModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ManagmentOrders(context: Context) {

    private val prefs = context.getSharedPreferences(
        "CoffeeShopOrders",
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

    fun getOrders(): ArrayList<OrderModel> {

        val json = prefs.getString("OrdersList", null)
            ?: return arrayListOf()

        val type = object :
            TypeToken<ArrayList<OrderModel>>() {}.type

        return try {
            gson.fromJson<ArrayList<OrderModel>>(json, type)
                ?: arrayListOf()
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    fun saveOrder(order: OrderModel) {

        val orders = getOrders()

        orders.add(0, order)

        prefs.edit()
            .putString("OrdersList", gson.toJson(orders))
            .apply()
    }


    fun deleteOrder(orderId: String) {

        val orders = getOrders()

        orders.removeAll {
            it.orderId == orderId
        }

        prefs.edit()
            .putString("OrdersList", gson.toJson(orders))
            .apply()
    }

}
