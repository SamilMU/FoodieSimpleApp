package com.example.foodbasket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.foodbasket.R
import com.example.foodbasket.databinding.LoginFragmentBinding
import com.example.foodbasket.entities.user.UserEntity

class LoginFragment : Fragment() {

    private lateinit var binding : LoginFragmentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

        binding.btnLogin.setOnClickListener {
            UserEntity.user_name = binding.etLoginName.text.toString()
            Navigation.findNavController(binding.btnLogin).navigate(R.id.login2FoodList)
        }

        return binding.root
    }

}