package com.example.project239.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project239.R
import com.example.project239.adapter.CategoryAdapter
import com.example.project239.adapter.PopularAdapter
import com.example.project239.databinding.ActivityMainBinding
import com.example.project239.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy { ViewModelProvider(this)[MainViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBanner()
        initCategory()
        initPopular()
        initBottomMenu()
    }

    override fun onResume() {
        super.onResume()
        selectBottomMenu("explorer")
    }

    private fun initBottomMenu() {
        binding.explorerBtn.setOnClickListener {
            selectBottomMenu("explorer")
        }

        binding.cartBtn.setOnClickListener {
            selectBottomMenu("cart")
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.wishlistBtn.setOnClickListener {
            selectBottomMenu("wishlist")
            startActivity(Intent(this, WishlistActivity::class.java))
        }

        binding.myOrderBtn.setOnClickListener {
            startActivity(
                Intent(this, MyOrdersActivity::class.java)
            )
        }

        binding.profileBtn.setOnClickListener {
            selectBottomMenu("profile")
        }
    }

    private fun selectBottomMenu(selected: String) {
        val menuItems = listOf(
            binding.explorerTxt,
            binding.cartTxt,
            binding.wishlistTxt,
            binding.myOrderTxt,
            binding.profileTxt
        )

        val unselectedColor = ContextCompat.getColor(this, R.color.darkBrown)
        val selectedColor = ContextCompat.getColor(this, R.color.orange)

        menuItems.forEach {
            it.setTypeface(null, Typeface.NORMAL)
            it.setTextColor(unselectedColor)
        }

        when (selected) {
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
    }

    private fun initPopular() {
        binding.progressBarPopular.visibility = View.VISIBLE
        viewModel.loadPopular().observe(this) { items ->
            binding.recyclerViewPopular.layoutManager = GridLayoutManager(this, 2)
            binding.recyclerViewPopular.adapter = PopularAdapter(items)
            binding.progressBarPopular.visibility = View.GONE
        }
    }

    private fun initCategory() {
        binding.progressBarCategory.visibility = View.VISIBLE
        binding.categoryView.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        viewModel.loadCategory().observe(this) { categories ->
            binding.categoryView.adapter = CategoryAdapter(categories)
            binding.progressBarCategory.visibility = View.GONE
        }
    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.GONE
        binding.banner.setImageResource(R.drawable.banner)
    }
}
