package com.example.foodbasket.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.foodbasket.R
import com.example.foodbasket.databinding.FoodDetailFragmentBinding
import com.example.foodbasket.entities.food.FoodEntity
import com.example.foodbasket.entities.user.UserEntity
import com.example.foodbasket.viewmodels.FoodDetailViewModel
import com.squareup.picasso.Picasso

class FoodDetailFragment : Fragment() {


    private lateinit var binding: FoodDetailFragmentBinding
    private lateinit var viewModel: FoodDetailViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.food_detail_fragment, container, false)

        val bundle : FoodDetailFragmentArgs by navArgs()
        val foodItem = bundle.foodItem

        Log.e("LogcatCA", foodItem.food_price.toString())

        // Views
        binding.foodObject = foodItem
        binding.tVUsername.text = UserEntity.user_name

        var quantity = 1
        binding.quantityObject = quantity

        binding.addButtonLottie.frame = 39
        binding.addButtonLottie.playAnimation()
        binding.subButtonLottie.frame = 39
        binding.subButtonLottie.playAnimation()

        // Buttons
        binding.subButtonLottie.setOnClickListener {
            quantity--
            binding.subButtonLottie.playAnimation()
            binding.quantityObject = quantity
        }

        binding.addButtonLottie.setOnClickListener {
            quantity++
            binding.addButtonLottie.playAnimation()
            binding.quantityObject = quantity
        }

        binding.addToCartLottie.setOnClickListener {
            binding.addToCartLottie.playAnimation()
            addItem2Cart(foodItem)
            quantity = 1
            binding.quantityObject = quantity
        }

        binding.bTCompleteOrder.setOnClickListener {
            Navigation.findNavController(binding.bTCompleteOrder).navigate(R.id.detail2Cart)
        }

        val url = "http://kasimadalan.pe.hu/yemekler/resimler/${foodItem.food_pic_name}"
        Picasso.get().load(url).into(binding.ivFoodDetail)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel : FoodDetailViewModel by viewModels()
        viewModel = tempViewModel
    }

    fun addItem2Cart(foodObject: FoodEntity){
        var quantity = Integer.parseInt(binding.etQuantityDetail.text.toString())
        if (quantity <= 0) quantity = 1
        viewModel.addFood2Cart(quantity,foodObject.food_price, foodObject.food_pic_name, foodObject.food_name, UserEntity.user_name)
    }

}