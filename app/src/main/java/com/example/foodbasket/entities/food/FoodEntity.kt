package com.example.foodbasket.entities.food

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FoodEntity(
    @SerializedName("yemek_id") @Expose var food_id: Int,
    @SerializedName("yemek_adi") @Expose var food_name: String,
    @SerializedName("yemek_resim_adi") @Expose var food_pic_name: String,
    @SerializedName("yemek_fiyat") @Expose var food_price: Int,
) : Serializable {
}