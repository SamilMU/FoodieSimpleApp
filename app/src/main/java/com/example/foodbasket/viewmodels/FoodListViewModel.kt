package com.example.foodbasket.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodbasket.adapters.FoodListAdapter
import com.example.foodbasket.entities.cart.CartEntity
import com.example.foodbasket.entities.food.FoodEntity
import com.example.foodbasket.entities.user.UserEntity
import com.example.foodbasket.repo.FoodRepository

class FoodListViewModel : ViewModel() {

    var foodList = MutableLiveData<List<FoodEntity>>()
    val foodRepo = FoodRepository()

    init {
        loadFoods()
        foodList = foodRepo.foodListReturner()
    }

    fun loadFoods() {
        foodRepo.getAllFoods()
    }

    fun addFood2Cart(
        food_order_quantity: Int,
        food_price: Int,
        food_pic_name: String,
        food_name: String,
        username: String
    ) {
        foodRepo.addFood2Cart(food_order_quantity, food_price, food_pic_name, food_name, username)
    }

    // Simple Combine Adder function located in FoodListRepo that iterates over a list that is needed to be add.
    // To be used in FoodListFragment for quick addition duplicates.
    // Not Used Now.
/*    fun addItems2Cart4Combiner(cartItemList: List<CartEntity>){
        foodRepo.addItems2Cart4Combiner(cartItemList)
    }*/

}