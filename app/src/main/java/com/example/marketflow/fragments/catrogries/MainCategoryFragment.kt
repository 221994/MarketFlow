package com.example.marketflow.fragments.catrogries

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketflow.R
import com.example.marketflow.adapters.BestDealsAdapter
import com.example.marketflow.adapters.BestProductsAdapter
import com.example.marketflow.adapters.SpecialProductsAdapter
import com.example.marketflow.databinding.FragmentMainCategoryBinding
import com.example.marketflow.utlities.Resource
import com.example.marketflow.utlities.showBottomNavigation
import com.example.marketflow.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductsAdapter: BestProductsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpecialProductsRecyclerView()
        setupBestProductsRecyclerView()
        setupBestDealsRecyclerView()
        specialProductsAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable("product", it) }
            val action = R.id.action_homeFragment_to_productDetailsFragment
            findNavController().navigate(action, bundle)

        }
        bestProductsAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable("product", it) }
            val action = R.id.action_homeFragment_to_productDetailsFragment
            findNavController().navigate(action, bundle)

        }
        bestDealsAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable("product", it) }
            val action = R.id.action_homeFragment_to_productDetailsFragment
            findNavController().navigate(action, bundle)
        }

        binding.nestedScrollViewMainCategory.setOnScrollChangeListener { v, _, scrollY, _, _ ->
            val contentView = binding.nestedScrollViewMainCategory.getChildAt(0)
            if (contentView != null) {
                if (v.height + scrollY >= contentView.height) {
                    viewModel.fetchBestProducts()
                }
            }
        }
        // Fetch data from Special Products
        lifecycleScope.launch {
            viewModel.specialProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                        Log.d("TAG", "Success Special products ${it.data?.size}")
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.d("TAG", "Error is ${it.message.toString()}")
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }
//         Fetch data from Best Products
        lifecycleScope.launch {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBarBestProducts.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        Log.d("TAG", "Success Best Products ${it.data?.size}")
                        binding.progressBarBestProducts.visibility = View.GONE
                    }

                    is Resource.Error -> {
                        binding.progressBarBestProducts.visibility = View.VISIBLE
                        Log.d("TAG", "Error is ${it.message.toString()}")
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }
        // Fetch data from Best Deals
        lifecycleScope.launch {
            viewModel.bestDeals.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }

                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        Log.d("TAG", "Success Best Deals ${it.data?.size}")
                        hideLoading()
                    }

                    is Resource.Error -> {
                        hideLoading()
                        Log.d("TAG", "Error is ${it.message.toString()}")
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> Unit
                }
            }
        }

    }

    private fun setupBestDealsRecyclerView() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealProductsBaseCategory.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter

        }
    }

    private fun setupBestProductsRecyclerView() {
        bestProductsAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    private fun showLoading() {
        binding.progressBarMainCategory.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBarMainCategory.visibility = View.INVISIBLE
    }

    private fun setupSpecialProductsRecyclerView() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }
}