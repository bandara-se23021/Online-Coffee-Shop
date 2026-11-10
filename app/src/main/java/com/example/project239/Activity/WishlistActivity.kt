package com.example.project239.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project239.Helper.ManagmentWishlist
import com.example.project239.adapter.WishlistAdapter
import com.example.project239.databinding.ActivityWishlistBinding

class WishlistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWishlistBinding
    private lateinit var wishlistManager: ManagmentWishlist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWishlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wishlistManager = ManagmentWishlist(this)

        binding.wishlistView.layoutManager =
            LinearLayoutManager(this)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
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
