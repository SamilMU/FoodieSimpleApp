package com.example.foodbasket.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.foodbasket.R
import com.example.foodbasket.adapters.FoodListAdapter
import com.example.foodbasket.databinding.FoodListFragmentBinding
import com.example.foodbasket.entities.user.UserEntity
import com.example.foodbasket.viewmodels.CartFragmentViewModel
import com.example.foodbasket.viewmodels.FoodListViewModel


class FoodListFragment : Fragment() {

    private lateinit var binding: FoodListFragmentBinding
    private lateinit var viewModel: FoodListViewModel
    private lateinit var viewModelCart: CartFragmentViewModel
    private lateinit var tempAdapter: FoodListAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.food_list_fragment ,container, false)

        // Username
        binding.tVUsername.text = UserEntity.user_name

        // Total Sum Calculation
        var tempCartSum = 0
        UserEntity.cartList.forEach {
            if(it.yemek_fiyat != null && it.yemek_fiyat!!>0){
                tempCartSum += (it.yemek_fiyat!!*it.yemek_siparis_adet!!)
            }
        }
        binding.cartPriceObject = tempCartSum

        // LiveData and RecyclerView
        viewModel.foodList.observe(viewLifecycleOwner) {
            val foodListAdapter = FoodListAdapter(requireContext(), it, viewModel)
            binding.foodListAdapterObject = foodListAdapter

            // Observer of Adapter LiveData for price update
            foodListAdapter.toolbarCart.observe(viewLifecycleOwner){
                var tempAdapterSum = 0
                it.forEach {
                    if(it.yemek_fiyat != null && it.yemek_fiyat!!>0){
                        tempAdapterSum += (it.yemek_fiyat!!*it.yemek_siparis_adet!!)
                    }
                    }
                binding.cartPriceObject = tempAdapterSum
            }
            tempAdapter = foodListAdapter

        }

        // Buttons
        binding.bTCompleteOrder.setOnClickListener {
/*
            // Cart items duplicate in case of fast clicking b4 db response.
            // Combine items before navigating to CartView.
            // FoodRepo has an instance of cartRepo. This has to be done on this or higher levels, lower is continous-loop.

            val commonCartItemList = viewModelCart.combineSeparateCartItems(viewModelCart.cartList.value!!)
            viewModel.addItems2Cart4Combiner(commonCartItemList)
*/

            tempAdapter.toolbarCart.value = arrayListOf()
            Navigation.findNavController(binding.bTCompleteOrder).navigate(R.id.foodList2Cart)
        }

        binding.bTCartToolbar.setOnClickListener {
            Navigation.findNavController(binding.bTCartToolbar).navigate(R.id.foodList2Cart)
        }

        return binding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel : FoodListViewModel by viewModels()
        val tempViewModelCart : CartFragmentViewModel by viewModels()
        viewModel = tempViewModel
        viewModelCart = tempViewModelCart
        // Cart and Total Sum Should be "GET" from DB so it can be set in required Views.
        viewModelCart.loadCart(UserEntity.user_name)
        Handler().postDelayed(this::onResume, 2000)

    }

    override fun onResume() {
        // Singleton Overrides Adapter Live for Food List Resumption.
        // Adapter LiveData updates around Singleton values.
        if(!UserEntity.cartList.isNullOrEmpty()){
            var tempCartSum = 0
            UserEntity.cartList.forEach {
                if(it.yemek_fiyat != null && it.yemek_fiyat!!>0){
                    tempCartSum += (it.yemek_fiyat!!*it.yemek_siparis_adet!!)
                }
            }
            binding.cartPriceObject = tempCartSum
        }else{
            binding.cartPriceObject = 0
        }
        super.onResume()
    }

}