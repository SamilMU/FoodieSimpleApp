package com.example.foodbasket.retrofit

class ApiUtils {

    companion object{
        val BASE_URL = "http://kasimadalan.pe.hu/"

        fun getFoodServiceDaoInterface() : FoodServiceDaoInterface {
            return RetrofitClient.getClient(BASE_URL).create(FoodServiceDaoInterface::class.java)
        }
    }
}