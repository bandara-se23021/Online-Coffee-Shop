package com.example.project239.Helper

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.example.project239.R
import com.example.project239.activity.CartActivity
import com.example.project239.activity.MainActivity
import com.example.project239.activity.MyOrdersActivity
import com.example.project239.activity.ProfileActivity
import com.example.project239.activity.WishlistActivity
import com.example.project239.databinding.LayoutBottomMenuBinding

fun setupBottomNavigation(
    activity: Activity,
    binding: LayoutBottomMenuBinding,
    activeMenu: String
) {
    val menuItems = listOf(
        binding.explorerTxt,
        binding.cartTxt,
        binding.wishlistTxt,
        binding.myOrderTxt,
        binding.profileTxt
    )

    val unselectedColor = ContextCompat.getColor(activity, R.color.darkBrown)
    val selectedColor = ContextCompat.getColor(activity, R.color.orange)

    menuItems.forEach {
        it.setTypeface(null, Typeface.NORMAL)
        it.setTextColor(unselectedColor)
    }

    when (activeMenu) {
        "explorer" -> {
            binding.explorerTxt.setTypeface(null, Typeface.BOLD)
            binding.explorerTxt.setTextColor(selectedColor)
        }
        "cart" -> {
            binding.cartTxt.setTypeface(null, Typeface.BOLD)
            binding.cartTxt.setTextColor(selectedColor)
        }
        "wishlist" -> {
            binding.wishlistTxt.setTypeface(null, Typeface.BOLD)
            binding.wishlistTxt.setTextColor(selectedColor)
        }
        "order" -> {
            binding.myOrderTxt.setTypeface(null, Typeface.BOLD)
            binding.myOrderTxt.setTextColor(selectedColor)
        }
        "profile" -> {
            binding.profileTxt.setTypeface(null, Typeface.BOLD)
            binding.profileTxt.setTextColor(selectedColor)
        }
    }

    binding.explorerBtn.setOnClickListener {
        if (activeMenu != "explorer") {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            activity.startActivity(intent)
        }
    }

    binding.cartBtn.setOnClickListener {
        if (activeMenu != "cart") {
            val intent = Intent(activity, CartActivity::class.java)
            activity.startActivity(intent)
        }
    }

    binding.wishlistBtn.setOnClickListener {
        if (activeMenu != "wishlist") {
            val intent = Intent(activity, WishlistActivity::class.java)
            activity.startActivity(intent)
        }
    }

    binding.myOrderBtn.setOnClickListener {
        if (activeMenu != "order") {
            val intent = Intent(activity, MyOrdersActivity::class.java)
            activity.startActivity(intent)
        }
    }

    binding.profileBtn.setOnClickListener {
        if (activeMenu != "profile") {
            val intent = Intent(activity, ProfileActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
