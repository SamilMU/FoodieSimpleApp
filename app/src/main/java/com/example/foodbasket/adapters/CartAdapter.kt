package com.example.foodbasket.adapters

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbasket.databinding.FoodCartCardBinding
import com.example.foodbasket.entities.cart.CartEntity
import com.example.foodbasket.entities.food.FoodEntity
import com.example.foodbasket.entities.user.UserEntity
import com.example.foodbasket.fragments.CartFragmentDirections
import com.example.foodbasket.viewmodels.CartFragmentViewModel
import com.example.foodbasket.viewmodels.FoodListViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class CartAdapter(var mContext:Context, var cart_list:List<CartEntity>,
                    var viewModel: CartFragmentViewModel,var viewModelList: FoodListViewModel)
                        : RecyclerView.Adapter<CartAdapter.cartCardViewHolder>() {

    inner class cartCardViewHolder(binding: FoodCartCardBinding) : RecyclerView.ViewHolder(binding.root){
            var binding : FoodCartCardBinding

            init {
                this.binding = binding
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartCardViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val binding = FoodCartCardBinding.inflate(layoutInflater, parent, false)
        return cartCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: cartCardViewHolder, position: Int) {
        val cartObject = cart_list.get(position)
        val binding = holder.binding

        // Views
        binding.tvFood.text = cartObject.yemek_adi
        binding.etQuantity.setText(cartObject.yemek_siparis_adet.toString())
        var quantity = Integer.parseInt(binding.etQuantity.text.toString())
        if (quantity <= 0) quantity = 1
        binding.tvPrice.text = "${cartObject.yemek_fiyat!!*quantity} ₺"
        binding.btnIncrease.frame = 39
        binding.btnDecrease.frame = 39

        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${cartObject.yemek_resim_adi}"
        Picasso.get().load(url).into(binding.ivFood)

        binding.tvItemCount.text = binding.etQuantity.text.toString()

        // Buttons
        binding.btnRemove.setOnClickListener {
            val adb: AlertDialog.Builder = AlertDialog.Builder(mContext)
            adb.setTitle("Foodie")
            adb.setMessage("${cartObject.yemek_adi} öğesini sepetten silmek istiyor musunuz ? ")
            adb.setPositiveButton("Evet", DialogInterface.OnClickListener { _, _ ->
                if(UserEntity.cartList.size <= 1){
                    viewModel.removeAllFoodFromCart(UserEntity.user_name)
                }else {
                    viewModel.removeFoodFromCart(cartObject.sepet_yemek_id!!, UserEntity.user_name)
                }
                Snackbar.make(binding.btnRemove,"${cartObject.yemek_adi} sepetten çıkarıldı",
                                                                        Snackbar.LENGTH_SHORT).show()
            })
            adb.setNegativeButton("Hayır",null)
            adb.show()
        }

        binding.btnIncrease.setOnClickListener {
            if (quantity <= 0) quantity = 1
            else quantity++
            binding.etQuantity.setText(quantity.toString())
            binding.btnIncrease.playAnimation()
            binding.tvPrice.text = "${cartObject.yemek_fiyat!!*quantity} ₺"
            binding.tvItemCount.text = binding.etQuantity.text.toString()
            viewModel.removeFoodFromCart(cartObject.sepet_yemek_id!!,UserEntity.user_name)
            cartUpdate(cartObject, binding.etQuantity.text.toString())
        }

        binding.btnDecrease.setOnClickListener {
            quantity--
            if (quantity <= 0) quantity = 1
            binding.etQuantity.setText(quantity.toString())
            binding.btnDecrease.playAnimation()
            binding.tvPrice.text = "${cartObject.yemek_fiyat!!*quantity} ₺"
            binding.tvItemCount.text = binding.etQuantity.text.toString()
            viewModel.removeFoodFromCart(cartObject.sepet_yemek_id!!,UserEntity.user_name)
            cartUpdate(cartObject, binding.etQuantity.text.toString())
        }

        // Card Row Click
        binding.foodCartRow.setOnClickListener {
            val tempFoodObject = FoodEntity(1,cartObject.yemek_adi!!,
                                                cartObject.yemek_resim_adi!!,cartObject.yemek_fiyat!!)
            val cart2FoodDetailNavDir = CartFragmentDirections.cart2FoodDetail(tempFoodObject)
            Log.e("LogcatCA", cartObject.yemek_fiyat.toString())
            Navigation.findNavController(it).navigate(cart2FoodDetailNavDir)
        }
    }

    override fun getItemCount(): Int {
        return cart_list.size
    }

    // Custom Function Used By Increase/Decrease Buttons Needed for Dynamic Changes in Cart Screen
    fun cartUpdate(cartObject : CartEntity, quantity : String) {
        var tempQuant = Integer.parseInt(quantity)
        if (tempQuant <= 0) tempQuant = 1
        val tempCartObject = cartObject
        tempCartObject.yemek_siparis_adet = tempQuant
        viewModelList.addFood2Cart(cartObject.yemek_siparis_adet!!,cartObject.yemek_fiyat!!,
                        cartObject.yemek_resim_adi!!,cartObject.yemek_adi!!,UserEntity.user_name)
    }

}