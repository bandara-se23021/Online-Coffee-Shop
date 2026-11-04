package com.example.project239.Activity

import com.example.project239.R
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.project239.Helper.ManagmentCart
import com.example.project239.databinding.ActivityDetailBinding
import com.example.project239.domain.ItemsModel

class DetailActivity : AppCompatActivity() {
    lateinit var  binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managementCart: ManagmentCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

       managementCart = ManagmentCart(this)

        bundle()
        initSizeList()

    }

    private fun initSizeList() {
        binding.apply {
            smallBtn.setOnClickListener {
                smallBtn.setBackgroundResource(R.drawable.brown_stroke_bg)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(0)
            }

            mediumBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(R.drawable.brown_stroke_bg)
                largeBtn.setBackgroundResource(0)
            }

            largeBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(R.drawable.brown_stroke_bg)
            }



        }

    }

    private fun bundle() {
        binding.apply {
            item = intent.getSerializableExtra("object") as ItemsModel

            if (item.picUrl.isNotEmpty()) {
                val picPath = item.picUrl[0]
                if (picPath.startsWith("http://") || picPath.startsWith("https://")) {
                    Glide.with(this@DetailActivity)
                        .load(picPath)
                        .placeholder(R.drawable.pop_2)
                        .error(R.drawable.pop_2)
                        .into(picMain)
                } else {
                    val cleanName = picPath.replace(".png", "").lowercase()
                    var drawableId = resources.getIdentifier(
                        cleanName,
                        "drawable",
                        packageName
                    )
                    if (drawableId == 0) {
                        drawableId = resources.getIdentifier(
                            "pop_$cleanName",
                            "drawable",
                            packageName
                        )
                    }
                    if (drawableId == 0) {
                        drawableId = resources.getIdentifier(
                            "btn_$cleanName",
                            "drawable",
                            packageName
                        )
                    }

                    if (drawableId != 0) {
                        picMain.setImageResource(drawableId)
                    } else {
                        Glide.with(this@DetailActivity)
                            .load(picPath)
                            .placeholder(R.drawable.pop_2)
                            .error(R.drawable.pop_2)
                            .into(picMain)
                    }
                }
            } else {
                picMain.setImageResource(R.drawable.pop_2)
            }

            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "$" + item.price
            ratingTxt.text = item.rating.toString()

            if (item.numberInCart < 1) {
                item.numberInCart = 1
            }
            numberInCartTxt.text = item.numberInCart.toString()

            addToCartBtn.setOnClickListener {
                managementCart.insertItems(item)
            }

            backBtn.setOnClickListener {
                finish()
            }

            plusBtn.setOnClickListener {
                item.numberInCart++
                numberInCartTxt.text = item.numberInCart.toString()
            }

            minusBtn.setOnClickListener {
                if (item.numberInCart > 1) {
                    item.numberInCart--
                    numberInCartTxt.text = item.numberInCart.toString()
                }
            }
        }
    }
}
