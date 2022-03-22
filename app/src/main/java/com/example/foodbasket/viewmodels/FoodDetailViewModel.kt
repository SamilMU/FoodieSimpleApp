package com.example.foodbasket.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodbasket.entities.food.FoodEntity
import com.example.foodbasket.repo.FoodRepository

class FoodDetailViewModel : ViewModel() {

    val foodRepo = FoodRepository()

    fun addFood2Cart(
        food_order_quantity: Int,
        food_price: Int,
        food_pic_name: String,
        food_name: String,
        username: String
    ) {
        foodRepo.addFood2Cart(food_order_quantity, food_price, food_pic_name, food_name, username)
    }

}