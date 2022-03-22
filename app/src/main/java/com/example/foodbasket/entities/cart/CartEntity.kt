package com.example.foodbasket.entities.cart

import com.google.firebase.database.IgnoreExtraProperties
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/** Entity'lerden fieldları alıp geçirmek mi daha mantıklı, çünkü cevapta nesne yok. Firebase açısından farklı olabilir. **/
@IgnoreExtraProperties
class CartEntity(
    var sepet_yemek_firebase_id: String? = "", //Not Used. Needed for Firebase Integration.
    @SerializedName("sepet_yemek_id") @Expose var sepet_yemek_id: Int? = 0,
    @SerializedName("yemek_adi") @Expose var yemek_adi: String? = "",
    @SerializedName("yemek_resim_adi") @Expose var yemek_resim_adi: String? = "",
    @SerializedName("yemek_fiyat") @Expose var yemek_fiyat: Int? = 0,
    @SerializedName("yemek_siparis_adet") @Expose var yemek_siparis_adet: Int? = 0,
    @SerializedName("kullanici_adi") @Expose var kullanici_adi: String? = ""
) {
}