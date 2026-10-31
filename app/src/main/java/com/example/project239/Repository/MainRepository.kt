package com.example.project239.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project239.domain.BannerModel
import com.example.project239.domain.CategoryModel
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
                } else {
                    val defaults = defaultCategories()
                    for (defaultCat in defaults) {
                        if (list.none { it.title.equals(defaultCat.title, ignoreCase = true) }) {
                            list.add(defaultCat)
                        }
                    }
                }
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                listData.value = defaultCategories()
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
}
