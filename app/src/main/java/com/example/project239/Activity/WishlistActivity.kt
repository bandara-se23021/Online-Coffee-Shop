package com.example.project239.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project239.Helper.ManagmentWishlist
import com.example.project239.Helper.setupBottomNavigation
import com.example.project239.R
import com.example.project239.adapter.WishlistAdapter
import com.example.project239.databinding.ActivityWishlistBinding

class WishlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistBinding
    private lateinit var wishlistManager: ManagmentWishlist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wishlistManager = ManagmentWishlist(this)
        binding.wishlistView.layoutManager = LinearLayoutManager(this)

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        setupBottomNavigation(this, binding.bottomMenu, "wishlist")
    }

    override fun onResume() {
        super.onResume()
        setupBottomNavigation(this, binding.bottomMenu, "wishlist")
        loadWishlist()
    }

    private fun loadWishlist() {
        val wishlistItems = wishlistManager.getListWishlist()

        binding.wishlistView.adapter = WishlistAdapter(
            wishlistItems,
            this
        ) {
            updateEmptyView()
        }

        updateEmptyView()
    }

    private fun updateEmptyView() {
        val isEmpty = wishlistManager.getListWishlist().isEmpty()

        binding.emptyTxt.visibility =
            if (isEmpty) View.VISIBLE else View.GONE

        binding.wishlistView.visibility =
            if (isEmpty) View.GONE else View.VISIBLE
    }
}
