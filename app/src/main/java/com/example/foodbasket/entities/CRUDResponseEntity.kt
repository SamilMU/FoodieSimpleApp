package com.example.foodbasket.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CRUDResponseEntity(
    @SerializedName("message") @Expose var message: String,
    @SerializedName("success") @Expose var success: Int
) {
}