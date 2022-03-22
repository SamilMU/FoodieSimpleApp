package com.example.foodbasket.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.foodbasket.entities.CRUDResponseEntity
import com.example.foodbasket.entities.cart.CartEntity
import com.example.foodbasket.entities.cart.CartResponseEntity
import com.example.foodbasket.entities.food.FoodEntity
import com.example.foodbasket.entities.user.UserEntity
import com.example.foodbasket.retrofit.ApiUtils
import com.example.foodbasket.retrofit.FoodServiceDaoInterface
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CartRepository {

    var cartList = MutableLiveData<List<CartEntity>>()
    var cartServiceDao: FoodServiceDaoInterface
    // Debug Needed.
    // Used for duplicate GET requests. Should be set True everywhere GET request is called.
    var initCheckCartRepo: Boolean
    /* Response may lag behind Get requests. Clearing Live Data before response first clears Cart.
    But response updates liveData and last items gets inserted back in. This bool is to prevent that*/
    var emptyCartCheckRepo: Boolean

    init {
        cartServiceDao = ApiUtils.getFoodServiceDaoInterface()
        cartList = MutableLiveData()
        initCheckCartRepo = true
        emptyCartCheckRepo = false
        Log.e("LogcatCRInit", "Here")
    }

    fun cartReturner(): MutableLiveData<List<CartEntity>> {
        return cartList
    }

    fun getCart(username: String) {
        cartServiceDao.pullUserCartWeb(username).enqueue(object : Callback<CartResponseEntity> {
            override fun onResponse(call: Call<CartResponseEntity>?, response: Response<CartResponseEntity>) {

                val responseList = response.body().cart_list
                // Update Singleton based on the Response.
                updateLocalCartList(responseList)
                // Update LiveData based on the response.
                if (initCheckCartRepo) {
                    cartList.value = responseList
                }
                // Single update ensurement.
                initCheckCartRepo = false

                // Cart Empty ensurement.
                if(emptyCartCheckRepo){
                    cartList.value = arrayListOf()
                    emptyCartCheckRepo = false
                }
            }

            override fun onFailure(call: Call<CartResponseEntity>?, t: Throwable?) {}
        })

    }

    fun removeFoodFromCart(cart_food_id: Int, username: String) {
        cartServiceDao.removeFoodFromCartWeb(cart_food_id, username)
            .enqueue(object : Callback<CRUDResponseEntity> {
                override fun onResponse(call: Call<CRUDResponseEntity>?, response: Response<CRUDResponseEntity>) {
                    initCheckCartRepo = true
                    getCart(UserEntity.user_name)
                }
                override fun onFailure(call: Call<CRUDResponseEntity>?, t: Throwable?) {}
            })
    }

    // LiveData loop for deleting every cart item.
    fun removeAllFoodFromCart(username: String) {
        if (!cartList.value.isNullOrEmpty()) {
            val tempCartItemList = cartList.value
            for (item in tempCartItemList!!) {
                removeFoodFromCart(item.sepet_yemek_id!!, username)
            }
        }
        // Clearing Singleton and LiveData.
        cartList.value = arrayListOf()
        UserEntity.cartList = arrayListOf()
        emptyCartCheckRepo = true
    }

    /** To be Implemented later. User DB and Food DB can be transferred for better performance **/
/*    fun uploadRetrofitCart2Firebase(cartItemList: List<CartEntity>){

        val tempCartListObject = cartItemList

        // Clear Database at first
        if(!cartItemList.isNullOrEmpty()){
            for(item in cartItemList){
                refCartItems.child(item.sepet_yemek_firebase_id!!).removeValue()
            }
        }

        // Add New Values
        if(tempCartListObject!!.size > 0){
            tempCartListObject.forEach {
                refCartItems.push().setValue(it)
            }
            cartList.value = tempCartListObject
        }
    }*/
    // Singleton Update. It clears and gets set to DB response because
    // Controls are made before sending Post requests to DB.
    fun updateLocalCartList(responseList: List<CartEntity>) {
        if (initCheckCartRepo) {
            UserEntity.cartList = arrayListOf()
            responseList.forEach {
                UserEntity.cartList.add(it)
            }
        }
    }

    // A Faulty Combiner Function to be Used in FoodListFragment to solve duplicates
    // in fast addition problem.
    // Not used now.
/*    fun combineSeparateCartItems(cartList: List<CartEntity>) : List<CartEntity> {
        if (!cartList.isNullOrEmpty()) {
            val tempCartList = ArrayList<CartEntity>()
            val cartItems2Add = ArrayList<CartEntity>()
            val cartItemIDs2Delete = ArrayList<Int>()
            var quantity = 0
            cartList.forEach { dbItem ->
                tempCartList.forEach {
                    if(dbItem.yemek_adi == it.yemek_adi){
                        quantity = dbItem.yemek_siparis_adet!! + it.yemek_siparis_adet!!
                        if(!cartItemIDs2Delete.contains(dbItem.sepet_yemek_id)){
                            cartItemIDs2Delete.add(dbItem.sepet_yemek_id!!)
                        }
                        if(!cartItemIDs2Delete.contains(it.sepet_yemek_id)){
                            cartItemIDs2Delete.add(it.sepet_yemek_id!!)
                        }
                    }
                }
                cartItems2Add.add(CartEntity("",1, dbItem.yemek_adi, dbItem.yemek_resim_adi, dbItem.yemek_fiyat,quantity,UserEntity.user_name))
                quantity = 0
                tempCartList.add(dbItem)
            }

            cartItemIDs2Delete.forEach {
                removeFoodFromCart(it,UserEntity.user_name)
            }


            return cartItems2Add
        }
        return arrayListOf()
    }*/
}