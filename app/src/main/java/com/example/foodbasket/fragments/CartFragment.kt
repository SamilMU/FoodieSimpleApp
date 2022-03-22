package com.example.foodbasket.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.foodbasket.R
import com.example.foodbasket.adapters.CartAdapter
import com.example.foodbasket.databinding.CartFragmentBinding
import com.example.foodbasket.entities.user.UserEntity
import com.example.foodbasket.viewmodels.CartFragmentViewModel
import com.example.foodbasket.viewmodels.FoodListViewModel

class CartFragment : Fragment() {

    // Food List ViewModel. Needed in adapter for item addition.
    private lateinit var viewModelList: FoodListViewModel

    private lateinit var binding: CartFragmentBinding
    private lateinit var viewModel: CartFragmentViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Databinding is used in RV. Rest uses viewBinding.
        binding = DataBindingUtil.inflate(inflater, R.layout.cart_fragment, container, false)

        // Toolbar Username
        binding.tVUsername.text = UserEntity.user_name

        // LiveData and RecyclerView
        viewModel.cartList.observe(viewLifecycleOwner) {
            val cartAdapter = CartAdapter(requireContext(),it, viewModel, viewModelList)
            binding.cartAdapterObject = cartAdapter
            // Total Cost Calculation
            var priceSum = 0
            val foodNameList = ArrayList<String>()
            for(i in it){
                priceSum += (i.yemek_fiyat!!*i.yemek_siparis_adet!!)
                foodNameList.add(i.yemek_adi!!)
            }
            // Bottom Result Views
            binding.tVPriceResult.text = "${priceSum} ₺"
            binding.cartPriceObject = priceSum
            binding.tVFoodListResult.text = foodNameList.toString().dropLast(1).drop(1)
            binding.tVItemCount.text = "Sepette ${foodNameList.size} ürün var"
        }

        // Buttons
        binding.bTAddFood.setOnClickListener {
            Navigation.findNavController(binding.bTAddFood).navigate(R.id.cart2FoodList)
        }

        binding.bTClear.setOnClickListener {
            viewModel.removeAllFoodFromCart(UserEntity.user_name)
        }

        binding.bTComplete.setOnClickListener {
            if(viewModel.cartList.value.isNullOrEmpty()){
                Toast.makeText(requireContext(),"Sepetiniz Boş. Lütfen ürün ekleyin!",Toast.LENGTH_SHORT).show()
            }else{
                Navigation.findNavController(binding.bTComplete).navigate(R.id.cart2Success)
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel : CartFragmentViewModel by viewModels()
        val tempViewModelList : FoodListViewModel by viewModels()
        viewModel = tempViewModel
        viewModelList = tempViewModelList
    }

}