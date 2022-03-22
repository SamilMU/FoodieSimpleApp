package com.example.foodbasket.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodbasket.entities.cart.CartEntity
import com.example.foodbasket.entities.cart.CartResponseEntity
import com.example.foodbasket.entities.user.UserEntity
import com.example.foodbasket.repo.CartRepository

class CartFragmentViewModel : ViewModel() {

    var cartList = MutableLiveData<List<CartEntity>>()
    var cartRepo = CartRepository()

    init {
        loadCart(UserEntity.user_name)
        cartList = cartRepo.cartReturner()
    }

    fun loadCart(username: String){
        cartRepo.getCart(username)
    }

    fun removeFoodFromCart(cart_food_id: Int, username: String){
        cartRepo.removeFoodFromCart(cart_food_id, username)
    }

    fun removeAllFoodFromCart(username: String)
    {
        cartRepo.removeAllFoodFromCart(username)
    }

    // A Faulty Combiner Function to be Used in FoodListFragment to solve duplicates
    // in fast addition problem.
    // Function is located in CartRepo
    // Not used now.
/*    fun combineSeparateCartItems(cartList: List<CartEntity>) : List<CartEntity> {
        val tempCartList = cartRepo.combineSeparateCartItems(cartList)
        return tempCartList
    }*/


}