package com.example.project239.Helper

import android.content.Context
import android.widget.Toast
import com.example.project239.domain.ItemsModel

class ManagmentWishlist(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertItem(item: ItemsModel) {
        val wishlist = getListWishlist()

        val exists = wishlist.any { it.title == item.title }

        if (!exists) {
            wishlist.add(item)
            tinyDB.putListObject("WishlistList", wishlist)

            Toast.makeText(
                context,
                "Added to Wishlist",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                "Already in Wishlist",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getListWishlist(): ArrayList<ItemsModel> {
        val rawList = tinyDB.getListObject("WishlistList") ?: arrayListOf()
        val cleanList = ArrayList<ItemsModel>()
        for (item in rawList) {
            if (item != null) {
                cleanList.add(item)
            }
        }
        return cleanList
    }

    fun removeItem(item: ItemsModel) {
        val wishlist = getListWishlist()
        wishlist.removeAll { it.title == item.title }
        tinyDB.putListObject("WishlistList", wishlist)
    }
}
