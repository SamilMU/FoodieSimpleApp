package com.example.foodbasket.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.example.foodbasket.R
import com.example.foodbasket.databinding.FragmentSuccessBinding
import com.example.foodbasket.entities.user.UserEntity
import com.google.android.material.snackbar.Snackbar

class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccessBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Only ViewBinding is used. No data is needed or used in this fragment.
        binding = FragmentSuccessBinding.inflate(inflater, container, false)

        binding.cartOrderConfirmedLottie.playAnimation()

        // Buttons
        binding.bTCart.setOnClickListener {
            Navigation.findNavController(binding.bTCart).navigate(R.id.success2Cart)
        }

        binding.bTCloseApp.setOnClickListener {
            val adb: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            adb.setTitle("Foodie")
            adb.setMessage("Gerçekten çıkmak istiyor musunuz? ")
            adb.setPositiveButton("Evet", DialogInterface.OnClickListener { _, _ ->
                requireActivity().finish()
            })
            adb.setNegativeButton("Hayır",null)
            adb.show()
        }

        binding.bTMenu.setOnClickListener {
            Navigation.findNavController(binding.bTMenu).navigate(R.id.success2FoodList)
        }

        return binding.root
    }

}