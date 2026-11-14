package com.example.project239.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project239.Helper.ManagmentOrders
import com.example.project239.Helper.setupBottomNavigation
import com.example.project239.R
import com.example.project239.adapter.MyOrdersAdapter
import com.example.project239.databinding.ActivityMyOrdersBinding

class MyOrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOrdersBinding
    private lateinit var ordersManager: ManagmentOrders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordersManager = ManagmentOrders(this)
        binding.ordersView.layoutManager = LinearLayoutManager(this)

        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }

        setupBottomNavigation(this, binding.bottomMenu, "order")
    }

    override fun onResume() {
        super.onResume()
        setupBottomNavigation(this, binding.bottomMenu, "order")
        loadOrders()
    }

    private fun loadOrders() {
        val orders = ordersManager.getOrders()

        val isEmpty = orders.isEmpty()
        binding.emptyTxt.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.ordersView.visibility = if (isEmpty) View.GONE else View.VISIBLE

        binding.ordersView.adapter = MyOrdersAdapter(orders){
            val currentOrders = ordersManager.getOrders()
            val isCurrentEmpty = currentOrders.isEmpty()

            binding.emptyTxt.visibility =
                if (isCurrentEmpty) View.VISIBLE else View.GONE

            binding.ordersView.visibility =
                if (isCurrentEmpty) View.GONE else View.VISIBLE
        }
    }
}
