package com.example.foodbasket.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.foodbasket.entities.CRUDResponseEntity
import com.example.foodbasket.entities.cart.CartEntity
import com.example.foodbasket.entities.food.FoodEntity
import com.example.foodbasket.entities.food.FoodResponseEntity
import com.example.foodbasket.entities.user.UserEntity
import com.example.foodbasket.retrofit.ApiUtils
import com.example.foodbasket.retrofit.FoodServiceDaoInterface
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodRepository {

    var foodList = MutableLiveData<List<FoodEntity>>()
    var foodServiceDao: FoodServiceDaoInterface
    var foodRepoCallExecuted = false
    // Used in DB Update. Items are removed from DB and new updated items are added to DB.
    var cartRepo = CartRepository()

    init {
        foodServiceDao = ApiUtils.getFoodServiceDaoInterface()
        foodList = MutableLiveData()
    }

    fun foodListReturner(): MutableLiveData<List<FoodEntity>> {
        return foodList
    }

    fun getAllFoods() {
        foodServiceDao.pullAllFoodWeb().enqueue(object : Callback<FoodResponseEntity> {
            override fun onResponse(call: Call<FoodResponseEntity>?, response: Response<FoodResponseEntity>) {
                val responseList = response.body().food_list
                foodList.value = responseList
            }
            override fun onFailure(call: Call<FoodResponseEntity>?, t: Throwable?) {}
        })
    }

    // Main Addition Function
    fun addFood2Cart(
        food_order_quantity: Int,
        food_price: Int,
        food_pic_name: String,
        food_name: String,
        username: String
    ) {
        // Reassignment to be used in case of a need to update.
        var food_cart_quantity = food_order_quantity

/*         Checking if Singleton contains the item that is being added.
         In case found, found item is deleted and it's quantity is added to new item to be
         added to DB. */
        val tempCartObject = CartEntity("",0,food_name,
            food_pic_name,food_price, food_cart_quantity, UserEntity.user_name)
        UserEntity.cartList.forEach {
            if(tempCartObject.yemek_adi == it.yemek_adi){
                food_cart_quantity += it.yemek_siparis_adet!!
                cartRepo.removeFoodFromCart(it.sepet_yemek_id!!, UserEntity.user_name)
            }
        }

        // Retrofit Post Request with reassigned quantity
        foodServiceDao.addFood2CartWeb(food_cart_quantity, food_price, food_pic_name, food_name, username).enqueue(object : Callback<CRUDResponseEntity> {

            override fun onResponse(call: Call<CRUDResponseEntity>?, response: Response<CRUDResponseEntity>) {
                foodRepoCallExecuted = true
                cartRepo.initCheckCartRepo = true
                cartRepo.getCart(username)
            }

            override fun onFailure(call: Call<CRUDResponseEntity>?, t: Throwable?) {}
        })

    }

    // Main Addition Function
    fun addFood2CartWithoutCheck(
        food_order_quantity: Int,
        food_price: Int,
        food_pic_name: String,
        food_name: String,
        username: String
    ) {
        var food_cart_quantity = food_order_quantity

        // Retrofit Post Request with reassigned quantity
        foodServiceDao.addFood2CartWeb(food_cart_quantity, food_price, food_pic_name, food_name, username).enqueue(object : Callback<CRUDResponseEntity> {

            override fun onResponse(call: Call<CRUDResponseEntity>?, response: Response<CRUDResponseEntity>) {
                foodRepoCallExecuted = true
                cartRepo.initCheckCartRepo = true
                cartRepo.getCart(username)
            }

            override fun onFailure(call: Call<CRUDResponseEntity>?, t: Throwable?) {}
        })

    }

    // Combine Adder function that iterates over a list that is needed to be add.
    // To be used in FoodListFragment for quick addition duplicates.
    // Not Used Now.
/*    fun addItems2Cart4Combiner(cartItemList: List<CartEntity>){
        cartItemList.forEach {
            addFood2Cart(it.yemek_siparis_adet!!, it.yemek_fiyat!!, it.yemek_resim_adi!!, it.yemek_adi!!, it.kullanici_adi!!)
        }
    }*/


}