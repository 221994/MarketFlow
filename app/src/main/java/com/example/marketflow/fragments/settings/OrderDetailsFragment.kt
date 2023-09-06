package com.example.marketflow.fragments.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketflow.adapters.BillingProductAdapter
import com.example.marketflow.data.order.OrderStatus
import com.example.marketflow.data.order.getOrderStatus
import com.example.marketflow.databinding.FragmentOrderDetailBinding
import com.example.marketflow.utlities.VerticalItemDecoration


class OrderDetailsFragment : Fragment() {
    private val navArgs by navArgs<OrderDetailsFragmentArgs>()
    private val billingProductAdapter by lazy { BillingProductAdapter() }
    private lateinit var binding: FragmentOrderDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        val order = navArgs.order
        binding.apply {
            tvOrderId.text = "Order #${order.orderID}"
            stepView.setSteps(
                mutableListOf(
                    OrderStatus.Ordered.status,
                    OrderStatus.Confirmed.status,
                    OrderStatus.Delivered.status,
                )
            )
            val currentOrderState = when (getOrderStatus(order.orderStatus)) {
                is OrderStatus.Ordered -> 0
                is OrderStatus.Confirmed -> 1
                is OrderStatus.Delivered -> 2
                else -> 0
            }
            stepView.go(currentOrderState, false)
            if (currentOrderState == 2) {
                stepView.done(true)
            }
            tvFullName.text = order.address.fullName
            tvAddress.text = "${order.address.street} ${order.address.city}"
            tvPhoneNumber.text = order.address.phone
            tvTotalPrice.text = "$ ${order.totalPrice}"
        }
        billingProductAdapter.differ.submitList(order.products)
    }

    private fun setupRecyclerView() {
        binding.rvProducts.apply {
            adapter = billingProductAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            addItemDecoration(VerticalItemDecoration())
        }
    }
}