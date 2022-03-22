package com.example.foodbasket.entities.cart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CartResponseEntity(
    @SerializedName("sepet_yemekler") @Expose var cart_list : List<CartEntity>,
    @SerializedName("yemekler") @Expose var success : Int
) {
}