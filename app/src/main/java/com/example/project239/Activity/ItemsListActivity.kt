package com.example.project239.activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.project239.R
import com.example.project239.adapter.ItemListCategoryAdapter
import com.example.project239.databinding.ActivityItemsListBinding
import com.example.project239.viewmodel.MainViewModel

class ItemsListActivity : AppCompatActivity() {
    lateinit var binding: ActivityItemsListBinding
    private val viewModel: MainViewModel by viewModels()
    private var id: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundles()
        initList()

    }

    private fun initList() {
        binding.apply {
            progressBar.visibility= View.VISIBLE
            viewModel.loadItems(id).observe(this@ItemsListActivity, Observer {
                listView.layoutManager=
                    GridLayoutManager(this@ItemsListActivity, 2)
                listView.adapter= ItemListCategoryAdapter(it)
                progressBar.visibility= View.GONE
            })
            backBtn.setOnClickListener { finish() }
        }
    }

    private fun getBundles() {
        id = intent.getStringExtra("id")!!
        title = intent.getStringExtra("title")!!
        binding.categoryTxt.text = title
    }

}