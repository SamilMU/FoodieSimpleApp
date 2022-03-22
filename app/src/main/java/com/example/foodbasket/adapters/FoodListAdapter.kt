package com.example.foodbasket.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.foodbasket.databinding.FoodListCardBinding
import com.example.foodbasket.entities.cart.CartEntity
import com.example.foodbasket.entities.food.FoodEntity
import com.example.foodbasket.entities.user.UserEntity
import com.example.foodbasket.fragments.FoodListFragmentDirections
import com.example.foodbasket.viewmodels.FoodListViewModel
import com.squareup.picasso.Picasso

class FoodListAdapter(var mContext: Context, var foodList: List<FoodEntity>,
                        var viewModelList: FoodListViewModel)
                            : RecyclerView.Adapter<FoodListAdapter.foodCardViewHolder>() {

    // LiveData Used by Increase Decrease Buttons, Needed for Dynamic Changes in Food List.
    // Observed in FoodListFragment.
    var toolbarCart = MutableLiveData<List<CartEntity>>()
    var listOfSelectedFoods = ArrayList<CartEntity>()

    inner class foodCardViewHolder(binding: FoodListCardBinding) :  RecyclerView.ViewHolder(binding.root) {
        var binding: FoodListCardBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): foodCardViewHolder {
        val layoutInflater = LayoutInflater.from(mContext)
        val binding = FoodListCardBinding.inflate(layoutInflater, parent, false)
        return foodCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: foodCardViewHolder, position: Int) {
        val foodObject = foodList.get(position)
        val binding = holder.binding

        // Views
        binding.tvFood.text = foodObject.food_name
        binding.tvPrice.text = " ${foodObject.food_price.toString()} ₺"
        binding.btnIncrease.frame = 39
        binding.btnDecrease.frame = 39
        if (binding.etQuantity.text.isEmpty()) {
            binding.etQuantity.setText("1")
        }
        var quantity = Integer.parseInt(binding.etQuantity.text.toString())

        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${foodObject.food_pic_name}"
        Picasso.get().load(url).into(binding.ivFood)

        // Buttons
        binding.btnAdd2Cart.setOnClickListener {
            binding.btnAdd2Cart.playAnimation()
            if (quantity <= 0) quantity = 1
            // 2B used by LiveData
            listOfSelectedFoods.add(
                CartEntity(
                    "", 1, foodObject.food_name,
                    foodObject.food_pic_name, foodObject.food_price, quantity, UserEntity.user_name
                )
            )
            // LiveData value change
            if(!UserEntity.cartList.isNullOrEmpty()){
                toolbarCart.value = listOfSelectedFoods + UserEntity.cartList
                listOfSelectedFoods.removeAll(listOfSelectedFoods)
            }else{
                toolbarCart.value = listOfSelectedFoods
                listOfSelectedFoods.removeAll(listOfSelectedFoods)
            }
            // Posting to DB via Retrofit.
            viewModelList.addFood2Cart(
                quantity,
                foodObject.food_price,
                foodObject.food_pic_name,
                foodObject.food_name,
                UserEntity.user_name
            )

        }

        binding.btnIncrease.setOnClickListener {
            binding.btnIncrease.playAnimation()
            if (binding.etQuantity.text.isEmpty()) {
                binding.etQuantity.setText("1")
            }
            if (quantity <= 0) quantity = 1
            quantity++
            binding.etQuantity.setText(quantity.toString())
        }

        binding.btnDecrease.setOnClickListener {
            binding.btnDecrease.playAnimation()
            if (binding.etQuantity.text.isEmpty()) {
                binding.etQuantity.setText("1")
            }
            quantity--
            if (quantity <= 0) quantity = 1
            binding.etQuantity.setText(quantity.toString())
        }

        // Food Card - Row
        binding.foodRowCard.setOnClickListener {
            val foodList2DetailNavDir = FoodListFragmentDirections.foodList2Detail(foodObject)
            Navigation.findNavController(it).navigate(foodList2DetailNavDir)
        }

    }

    override fun getItemCount(): Int {
        return foodList.size
    }


}