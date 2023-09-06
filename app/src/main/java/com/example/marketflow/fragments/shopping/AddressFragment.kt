package com.example.marketflow.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.marketflow.data.Address
import com.example.marketflow.databinding.FragmentAddressBinding
import com.example.marketflow.utlities.Resource
import com.example.marketflow.viewmodel.AddressViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddressFragment : Fragment() {
    private val args by navArgs<AddressFragmentArgs>()
    private val viewModel by viewModels<AddressViewModel>()
    lateinit var binding: FragmentAddressBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val address = args.address
        if (address == null) {
            binding.buttonDelelte.visibility = View.GONE
        } else {
            binding.apply {
                edAddressTitle.setText(address.addressTitle)
                edFullName.setText(address.fullName)
                edState.setText(address.state)
                edPhone.setText(address.phone)
                edCity.setText(address.city)
                edStreet.setText(address.street)
            }
            binding.apply {
                buttonSave.setOnClickListener {
                    val addressTitle = edAddressTitle.text.toString()
                    val fullName = edFullName.text.toString()
                    val street = edStreet.text.toString()
                    val state = edState.text.toString()
                    val phone = edPhone.text.toString()
                    val city = edCity.text.toString()
                    val address = Address(addressTitle, fullName, street, phone, city, state)
                    viewModel.addAddress(address)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.addNewAddress.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.INVISIBLE
                        findNavController().navigateUp()

                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit

                }
            }
        }
        lifecycleScope.launch {
            viewModel.errorMessage.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
        binding.apply {
            buttonSave.setOnClickListener {
                val addressTitle = edAddressTitle.text.toString()
                val fullName = edFullName.text.toString()
                val street = edStreet.text.toString()
                val phone = edPhone.text.toString()
                val city = edCity.text.toString()
                val state = edState.text.toString()
                val address = Address(addressTitle, fullName, street, phone, city, state)
                viewModel.addAddress(address)
            }
        }
    }
}