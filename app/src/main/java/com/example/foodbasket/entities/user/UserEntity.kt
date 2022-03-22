package com.example.foodbasket.entities.user

import com.example.foodbasket.entities.cart.CartEntity

//Singleton
object UserEntity {
    var user_name: String = ""
    // Local stored Cart in case of delay in DB responses. DataStore may be better.
    var cartList : ArrayList<CartEntity> = arrayListOf()
}