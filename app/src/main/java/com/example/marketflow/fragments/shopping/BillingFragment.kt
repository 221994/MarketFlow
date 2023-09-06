package com.example.marketflow.fragments.shopping

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketflow.R
import com.example.marketflow.adapters.AddressAdapter
import com.example.marketflow.adapters.BillingProductAdapter
import com.example.marketflow.data.Address
import com.example.marketflow.data.CartProduct
import com.example.marketflow.databinding.FragmentBillingBinding
import com.example.marketflow.utlities.HorizontalItemDecoration
import com.example.marketflow.utlities.Resource
import com.example.marketflow.viewmodel.BillingViewModel
import com.example.marketflow.viewmodel.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.example.marketflow.data.order.Order
import com.example.marketflow.data.order.OrderStatus
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment : Fragment() {
    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductAdapter by lazy { BillingProductAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()
    private var totalPrice = 0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBillingBinding.inflate(layoutInflater)
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        products = args.products.toList()
        totalPrice = args.totalPrice
        setupAddressRV()
        setupBillingRV()
        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        if (!args.payment) {
            binding.apply {
                buttonPlaceOrder.visibility = View.INVISIBLE
                totalBoxContainer.visibility = View.INVISIBLE
                middleLine.visibility = View.INVISIBLE
                bottomLine.visibility = View.INVISIBLE
            }
        }

        lifecycleScope.launch {
            billingViewModel.address.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        addressAdapter.differ.submitList(it.data)
                        binding.progressbarAddress.visibility = View.GONE
                    }
                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(
                            requireContext(), "error is ${it.message.toString()}", Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> Unit
                }
            }
            lifecycleScope.launch {
                orderViewModel.order.collectLatest {
                    when (it) {
                        is Resource.Loading -> {
                            binding.buttonPlaceOrder.startAnimation()
                        }

                        is Resource.Success -> {
                            binding.buttonPlaceOrder.revertAnimation()
                            findNavController().navigateUp()
                            Snackbar.make(
                                requireView(), "Your order was placed!", Snackbar.LENGTH_LONG
                            ).show()
                        }

                        is Resource.Error -> {
                            binding.buttonPlaceOrder.revertAnimation()
                            Toast.makeText(
                                requireContext(),
                                "error is ${it.message.toString()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        else -> Unit
                    }
                }
            }
        }
        billingProductAdapter.differ.submitList(products)
        binding.tvTotalPrice.text = "$ $totalPrice"
        addressAdapter.onClick = {
            selectedAddress = it
            val bundle = Bundle()
            bundle.putParcelable("address", selectedAddress)
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment, bundle)
        }

        binding.buttonPlaceOrder.setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(requireContext(), "Please selected an Address", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            showConfirmationOrderDialog()
        }
    }

    private fun showConfirmationOrderDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("Order items").setMessage("Do you want to order your cart items?")
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }.setPositiveButton("Yes") { dialog, _ ->
                    val order =
                        Order(OrderStatus.Ordered.status, totalPrice, products, selectedAddress!!)
                    orderViewModel.placeOrder(order)
                    dialog.dismiss()
                }
        }
        alertDialog.create()
        alertDialog.show()
    }

    private fun setupAddressRV() {
        binding.rvAddress.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupBillingRV() {
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = billingProductAdapter
            addItemDecoration(HorizontalItemDecoration())

        }
    }


}