package com.example.project239.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        wishlistManager = ManagmentWishlist(this)
        binding.wishlistView.layoutManager = LinearLayoutManager(this)

        binding.backBtn.setOnClickListener {
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
