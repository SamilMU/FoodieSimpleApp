package com.example.foodbasket.retrofit

import com.example.foodbasket.entities.CRUDResponseEntity
import com.example.foodbasket.entities.cart.CartResponseEntity
import com.example.foodbasket.entities.food.FoodResponseEntity
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface FoodServiceDaoInterface {
    /** http://kasimadalan.pe.hu/yemekler/ **/

    @GET("yemekler/tumYemekleriGetir.php")
    fun pullAllFoodWeb(): Call<FoodResponseEntity>

    @GET("yemekler/tumSepettekiYemekleriGetir.php")
    fun pullAllCartsWeb(): Call<CartResponseEntity>

    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    fun pullUserCartWeb(@Field("kullanici_adi") username: String): Call<CartResponseEntity>

    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    fun addFood2CartWeb(
        @Field("yemek_siparis_adet") food_order_quantity: Int,
        @Field("yemek_fiyat") food_price:Int,
        @Field("yemek_resim_adi") food_pic_name:String,
        @Field("yemek_adi") food_name: String,
        @Field("kullanici_adi") username: String): Call<CRUDResponseEntity>


    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    fun removeFoodFromCartWeb(
        @Field("sepet_yemek_id") cart_food_id: Int,
        @Field("kullanici_adi") username: String): Call<CRUDResponseEntity>

}