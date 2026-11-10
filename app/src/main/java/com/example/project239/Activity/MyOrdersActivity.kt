
package com.example.project239.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project239.Helper.ManagmentOrders
import com.example.project239.adapter.MyOrdersAdapter
import com.example.project239.databinding.ActivityMyOrdersBinding

class MyOrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOrdersBinding
    private lateinit var ordersManager: ManagmentOrders

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ordersManager = ManagmentOrders(this)

        binding.ordersView.layoutManager =
            LinearLayoutManager(this)

        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }

    private fun loadOrders() {

        val orders = ordersManager.getOrders()

        binding.ordersView.adapter =
            MyOrdersAdapter(orders)

        binding.emptyTxt.visibility =
            if (orders.isEmpty()) View.VISIBLE else View.GONE

        binding.ordersView.visibility =
            if (orders.isEmpty()) View.GONE else View.VISIBLE
    }
}
