package com.example.foodbasket.entities.food

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FoodResponseEntity(
    @SerializedName("yemekler") @Expose var food_list: List<FoodEntity>,
    @SerializedName("success") @Expose var success: Int) {
}