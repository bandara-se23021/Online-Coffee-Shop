package com.example.project239.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project239.Helper.ChangeNumberItemsListener
import com.example.project239.Helper.ManagmentCart
import com.example.project239.Helper.ManagmentOrders
import com.example.project239.Helper.setupBottomNavigation
import com.example.project239.adapter.CartAdapter
import com.example.project239.databinding.ActivityCartBinding
import com.example.project239.domain.OrderModel
import java.util.UUID

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var managementCart: ManagmentCart
    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagmentCart(this)

        setVariable()
        initCartList()
        calculateCart()

        setupBottomNavigation(this, binding.bottomMenu, "cart")

        binding.button.setOnClickListener {
            placeOrder()
        }
    }

    override fun onResume() {
        super.onResume()
        setupBottomNavigation(this, binding.bottomMenu, "cart")
    }

    private fun initCartList() {
        binding.apply {
            listView.layoutManager =
                LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
            listView.adapter = CartAdapter(
                managementCart.getListCart(),
                this@CartActivity,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        calculateCart()
                    }
                }
            )
        }
    }

    private fun setVariable() {
        binding.backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val isCartEmpty = managementCart.getListCart().isEmpty()
        val delivery = if (isCartEmpty) 0.0 else 15.0
        val totalFee = managementCart.getTotalFee()

        tax = Math.round(totalFee * percentTax * 100.0) / 100.0
        val total = Math.round((totalFee + tax + delivery) * 100.0) / 100.0
        val itemTotal = Math.round(totalFee * 100.0) / 100.0

        binding.apply {
            totalFeeTxt.text = "$$itemTotal"
            totalTaxTxt.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$$total"
        }
    }

    private fun placeOrder() {
        val cartItems = managementCart.getListCart()

        if (cartItems.isEmpty()) {
            Toast.makeText(
                this,
                "Your Cart is Empty",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val subtotal = managementCart.getTotalFee()
        val orderTax = Math.round(subtotal * 0.02 * 100.0) / 100.0
        val delivery = 15.0
        val total = Math.round(
            (subtotal + orderTax + delivery) * 100.0
        ) / 100.0

        val order = OrderModel(
            orderId = UUID.randomUUID()
                .toString()
                .take(8)
                .uppercase(),
            orderDate = System.currentTimeMillis(),
            items = ArrayList(cartItems),
            totalPrice = total,
            status = "Pending"
        )

        val ordersManager = ManagmentOrders(this)
        ordersManager.saveOrder(order)

        managementCart.clearCart()

        Toast.makeText(
            this,
            "Order Placed Successfully",
            Toast.LENGTH_SHORT
        ).show()

        val intent = Intent(
            this,
            MyOrdersActivity::class.java
        )

        startActivity(intent)
        finish()
    }
}
