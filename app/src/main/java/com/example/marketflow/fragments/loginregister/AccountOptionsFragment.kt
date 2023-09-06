package com.example.marketflow.fragments.loginregister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marketflow.R
import com.example.marketflow.databinding.FragmentAccountOptionsBinding


class AccountOptionsFragment : Fragment() {
    private lateinit var binding: FragmentAccountOptionsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountOptionsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonLoginAccountOptions.setOnClickListener {
            val action = R.id.action_accountOptionsFragment_to_loginFragment
            findNavController().navigate(action)
        }
        binding.buttonRegisterAccountOptions.setOnClickListener {
            val action = R.id.action_accountOptionsFragment_to_registerFragment
            findNavController().navigate(action)
        }
    }


}