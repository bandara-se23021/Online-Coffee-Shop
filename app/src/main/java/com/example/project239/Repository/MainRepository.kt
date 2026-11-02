package com.example.project239.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project239.domain.BannerModel
import com.example.project239.domain.CategoryModel
import com.example.project239.domain.ItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainRepository {
    private val firebaseDatabase = FirebaseDatabase.getInstance(
        "https://project239-6dca8-default-rtdb.firebaseio.com/",
    )

    fun loadBanner(): LiveData<MutableList<BannerModel>> {
        val listData = MutableLiveData<MutableList<BannerModel>>()
        val ref = firebaseDatabase.getReference("Banner")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<BannerModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(BannerModel::class.java)
                    item?.let { list.add(it) }
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                listData.value = mutableListOf()
            }
        })
        return listData
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        val listData = MutableLiveData<MutableList<CategoryModel>>()
        val ref = firebaseDatabase.getReference("Category")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<CategoryModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(CategoryModel::class.java)
                    item?.let { list.add(it) }
                }
                if (list.isEmpty()) {
                    list.addAll(defaultCategories())
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                listData.value = defaultCategories()
            }
        })
        return listData
    }

    fun loadPopular(): LiveData<MutableList<ItemsModel>> {
        val listData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ItemsModel>()
                for (childSnapshot in snapshot.children) {
                    val item = childSnapshot.getValue(ItemsModel::class.java)
                    item?.let { list.add(it) }
                }
                if (list.isEmpty()) {
                    val refPopular = firebaseDatabase.getReference("Popular")
                    refPopular.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(popSnapshot: DataSnapshot) {
                            val popList = mutableListOf<ItemsModel>()
                            for (c in popSnapshot.children) {
                                val item = c.getValue(ItemsModel::class.java)
                                item?.let { popList.add(it) }
                            }
                            if (popList.isEmpty()) {
                                listData.value = defaultPopular()
                            } else {
                                listData.value = popList
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            listData.value = defaultPopular()
                        }
                    })
                } else {
                    listData.value = list
                }
            }

            override fun onCancelled(error: DatabaseError) {
                listData.value = defaultPopular()
            }
        })
        return listData
    }

    private fun defaultCategories(): MutableList<CategoryModel> {
        return mutableListOf(
            CategoryModel(0, "Esspersso"),
            CategoryModel(1, "Cappuccino"),
            CategoryModel(2, "Latte"),
            CategoryModel(3, "Americano"),
            CategoryModel(4, "Hot Chocolate")
        )
    }

    private fun defaultPopular(): MutableList<ItemsModel> {
        return mutableListOf(
            ItemsModel(
                title = "Cappuccino",
                description = "Cappuccino is an Italian coffee drink that is traditionally prepared with equal parts double espresso, steamed milk, and steamed milk foam on top.",
                picUrl = arrayListOf("pop_2"),
                price = 4.5,
                rating = 4.8,
                numberInCart = 0,
                extra = "Espresso,Milk"
            ),
            ItemsModel(
                title = "Espresso",
                description = "Espresso is a concentrated form of coffee served in small, strong shots.",
                picUrl = arrayListOf("pop_5"),
                price = 3.5,
                rating = 4.9,
                numberInCart = 0,
                extra = "Espresso"
            ),
            ItemsModel(
                title = "Affagato",
                description = "An affogato is an Italian coffee-based dessert.",
                picUrl = arrayListOf("pop_3"),
                price = 8.0,
                rating = 4.7,
                numberInCart = 0,
                extra = "Espresso,milk,IceCream"
            ),
            ItemsModel(
                title = "Americano",
                description = "An Americano is an espresso drink made with hot water and espresso.",
                picUrl = arrayListOf("pop_4"),
                price = 5.5,
                rating = 4.6,
                numberInCart = 0,
                extra = "Espresso,milk"
            )
        )
    }

    private fun defaultCategoryItems(categoryId: String): MutableList<ItemsModel> {
        return when (categoryId) {
            "0" -> mutableListOf(
                ItemsModel(
                    title = "Espresso",
                    description = "Espresso is a concentrated form of coffee served in small, strong shots.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598194/5_gdnijm.png"),
                    price = 3.5,
                    rating = 4.0,
                    numberInCart = 0,
                    extra = "Espresso",
                    categoryId = "0"
                ),
                ItemsModel(
                    title = "Light Espresso",
                    description = "A lighter espresso shot with a smoother finish.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598194/7_hpl7t1.png"),
                    price = 3.5,
                    rating = 4.0,
                    numberInCart = 0,
                    extra = "Espresso",
                    categoryId = "0"
                )
            )
            "1" -> mutableListOf(
                ItemsModel(
                    title = "Pumpkin Latte",
                    description = "A seasonal favorite with pumpkin spice and steamed milk.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598183/2_iudehq.png"),
                    price = 5.5,
                    rating = 4.6,
                    numberInCart = 0,
                    extra = "Espresso, steamed milk, pumpkin spice syrup",
                    categoryId = "1"
                ),
                ItemsModel(
                    title = "Macchiato",
                    description = "A bold espresso drink topped with a small amount of milk foam.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598189/4_vcxvtv.png"),
                    price = 3.5,
                    rating = 4.6,
                    numberInCart = 0,
                    extra = "Espresso, milk foam",
                    categoryId = "1"
                ),
                ItemsModel(
                    title = "Matcha Latte",
                    description = "Matcha green tea with steamed milk.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598184/3_o0g3jz.png"),
                    price = 5.0,
                    rating = 4.6,
                    numberInCart = 0,
                    extra = "Matcha powder, steamed milk",
                    categoryId = "1"
                ),
                ItemsModel(
                    title = "Cortado",
                    description = "A balanced blend of espresso and warm milk.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598179/1_zgqdzu.png"),
                    price = 3.75,
                    rating = 4.6,
                    numberInCart = 0,
                    extra = "Espresso, steamed milk",
                    categoryId = "1"
                )
            )
            "2" -> mutableListOf(
                ItemsModel(
                    title = "Caramel Latte",
                    description = "Rich espresso with steamed milk and sweet caramel syrup.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748601130/10_hurlfp.png"),
                    price = 4.8,
                    rating = 4.7,
                    numberInCart = 0,
                    extra = "Espresso, steamed milk, caramel syrup",
                    categoryId = "2"
                ),
                ItemsModel(
                    title = "Vanilla Latte",
                    description = "Espresso combined with creamy steamed milk and vanilla flavor.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748601130/9_vgwjdj.png"),
                    price = 4.5,
                    rating = 4.8,
                    numberInCart = 0,
                    extra = "Espresso, steamed milk, vanilla",
                    categoryId = "2"
                )
            )
            "3" -> mutableListOf(
                ItemsModel(
                    title = "Caffe Americano",
                    description = "Espresso diluted with hot water for a smooth coffee flavor.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598188/6_syir34.png"),
                    price = 3.8,
                    rating = 4.5,
                    numberInCart = 0,
                    extra = "Espresso, hot water",
                    categoryId = "3"
                ),
                ItemsModel(
                    title = "Iced Americano",
                    description = "Chilled espresso and cold water served over ice.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748598188/6_syir34.png"),
                    price = 4.0,
                    rating = 4.6,
                    numberInCart = 0,
                    extra = "Espresso, cold water, ice",
                    categoryId = "3"
                )
            )
            "4" -> mutableListOf(
                ItemsModel(
                    title = "Classic Hot Chocolate",
                    description = "Rich chocolate blended with steamed milk and topped with foam.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748601121/8_czlhri.png"),
                    price = 4.2,
                    rating = 4.9,
                    numberInCart = 0,
                    extra = "Chocolate, steamed milk",
                    categoryId = "4"
                ),
                ItemsModel(
                    title = "Marshmallow Hot Chocolate",
                    description = "Creamy hot chocolate topped with fluffy marshmallows.",
                    picUrl = arrayListOf("https://res.cloudinary.com/dkikc5ywq/image/upload/v1748601128/13_nvrg2g.png"),
                    price = 4.8,
                    rating = 4.9,
                    numberInCart = 0,
                    extra = "Chocolate, steamed milk, marshmallows",
                    categoryId = "4"
                )
            )
            else -> mutableListOf()
        }
    }

    fun loadItemCategory(categoryId: String): LiveData<MutableList<ItemsModel>> {
        val itemsLiveData = MutableLiveData<MutableList<ItemsModel>>()
        val ref = firebaseDatabase.getReference("Items")

        val fetchWithQuery = { q: Query, onEmpty: () -> Unit ->
            q.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<ItemsModel>()
                    for (childSnapshot in snapshot.children) {
                        val item = childSnapshot.getValue(ItemsModel::class.java)
                        item?.let { list.add(it) }
                    }
                    if (list.isNotEmpty()) {
                        itemsLiveData.value = list
                    } else {
                        onEmpty()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    onEmpty()
                }
            })
        }

        fetchWithQuery(ref.orderByChild("categoryId").equalTo(categoryId)) {
            val numValue = categoryId.toDoubleOrNull()
            if (numValue != null) {
                fetchWithQuery(ref.orderByChild("categoryId").equalTo(numValue)) {
                    itemsLiveData.value = defaultCategoryItems(categoryId)
                }
            } else {
                itemsLiveData.value = defaultCategoryItems(categoryId)
            }
        }

        return itemsLiveData
    }

}
