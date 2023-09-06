package com.example.marketflow.fragments.shopping

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marketflow.R
import com.example.marketflow.adapters.ColorsAdapter
import com.example.marketflow.adapters.SizesAdapter
import com.example.marketflow.adapters.ViewPagerAdapterImages
import com.example.marketflow.data.CartProduct
import com.example.marketflow.databinding.FragmentProductDetailsBinding
import com.example.marketflow.utlities.Resource
import com.example.marketflow.utlities.hideBottomNavigation
import com.example.marketflow.viewmodel.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewpagerAdapter by lazy { ViewPagerAdapterImages() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorAdapter by lazy { ColorsAdapter() }
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val viewModel by viewModels<DetailsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        hideBottomNavigation()
        binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product
        binding.apply {
            tvProductNameDetailsFragment.text = product.name
            tvProductPriceDetailsFragment.text = "$ ${product.price}"
            tvProductDescriptionDetailsFragment.text = product.description
            if (product.colors.isNullOrEmpty()) {
                binding.tvColor.visibility = View.GONE
            }
            if (product.sizes.isNullOrEmpty()) {
                binding.tvSize.visibility = View.GONE
            }
        }
        viewpagerAdapter.differ.submitList(product.images)
        product.colors?.let {
            colorAdapter.differ.submitList(it)
        }
        product.sizes?.let {
            sizesAdapter.differ.submitList(it)
        }
        setupColorsRV()
        setupSizesRV()
        setupViewPager()
        binding.ivCloseImageInsideCardView.setOnClickListener {
            findNavController().navigateUp()
        }
        sizesAdapter.onItemClick = {
            selectedSize = it
        }
        colorAdapter.onItemClick = {
            selectedColor = it
        }
        // we gonna update it later to make the user select the color and the size
        binding.addToCardButtonDetailsFragment.setOnClickListener {
            viewModel.addUpdateProductInCart(CartProduct(product, 1, selectedColor, selectedSize))
        }
        lifecycleScope.launch {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.addToCardButtonDetailsFragment.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.addToCardButtonDetailsFragment.revertAnimation()
                        binding.addToCardButtonDetailsFragment.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(), R.color.black
                            )
                        )
                    }

                    is Resource.Error -> {
                        binding.addToCardButtonDetailsFragment.revertAnimation()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }

                    else -> {
                        Unit
                    }
                }
            }
        }
    }

    private fun setupViewPager() {
        binding.apply {
            viewPagerProductDetailsFragment.adapter = viewpagerAdapter
        }
    }

    private fun setupColorsRV() {
        binding.rvColorProductDetails.apply {
            adapter = colorAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupSizesRV() {
        binding.rvSizesProductDetails.apply {
            adapter = sizesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }
}