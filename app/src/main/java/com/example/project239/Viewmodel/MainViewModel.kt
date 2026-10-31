package com.example.project239.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.project239.domain.BannerModel
import com.example.project239.domain.CategoryModel
import com.example.project239.repository.MainRepository

class MainViewModel : ViewModel() {

    private val repository = MainRepository()

    fun loadBanner(): LiveData<MutableList<BannerModel>> {
        return repository.loadBanner()
    }

    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }
}
