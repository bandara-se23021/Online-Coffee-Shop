package com.example.project239.activity


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.project239.Adapter.CategoryAdapter
import com.example.project239.viewmodel.MainViewModel
import com.example.project239.databinding.ActivityMainBinding

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

    }

    private fun initBanner() {
        binding.progressBarBanner.visibility = View.VISIBLE
        viewModel.loadBanner().observe(this) { banners ->
            val url = banners.firstOrNull()?.url?.takeIf { it.isNotBlank() }
            if (url == null) {
                Log.w("FirebaseBanner", "No banner URL found")
                binding.progressBarBanner.visibility = View.GONE
                return@observe
            }
            Glide.with(this)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean,
                    ): Boolean {
                        Log.e("FirebaseBanner", "Image load failed: $url", e)
                        binding.progressBarBanner.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        binding.progressBarBanner.visibility = View.GONE
                        return false
                    }
                })
                .into(binding.banner)
        }
    }


    private fun initCategory() {

        binding.progressBarCategory.visibility = View.VISIBLE

        binding.categoryView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        viewModel.loadCategory().observe(this) { categories ->

            binding.progressBarCategory.visibility = View.GONE

            if (categories.isNullOrEmpty()) {
                Log.w("FirebaseCategory", "No categories found")
                return@observe
            }

            Log.d(
                "FirebaseCategory",
                "Loaded categories: ${categories.size}"
            )

            binding.categoryView.adapter = CategoryAdapter(
                categories
            )
        }
    }

}





