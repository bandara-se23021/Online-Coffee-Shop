package com.example.project239.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project239.domain.BannerModel
import com.example.project239.domain.CategoryModel
import com.example.project239.domain.ItemsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
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
            CategoryModel(1, "Esspersso"),
            CategoryModel(2, "Cappuccino"),
            CategoryModel(3, "Latte"),
            CategoryModel(4, "Americano"),
            CategoryModel(5, "Hot Chocolate")
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
}
