package com.example.marketflow.fragments.catrogries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marketflow.R
import com.example.marketflow.adapters.BestProductsAdapter
import com.example.marketflow.databinding.FragmentBaseCategoryBinding
import com.example.marketflow.utlities.showBottomNavigation

open class BaseCategoryFragment : Fragment() {
    lateinit var binding: FragmentBaseCategoryBinding
    protected val bestProductsAdapterWithOffer: BestProductsAdapter by lazy { BestProductsAdapter() }
    protected val bestProductsAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBestProductsWithOfferRv()
        setupBestDealsRv()
        bestProductsAdapterWithOffer.onClick = {
            val bundle = Bundle().apply { putParcelable("product", it) }
            val action = R.id.action_homeFragment_to_productDetailsFragment
            findNavController().navigate(action, bundle)
        }
        bestProductsAdapter.onClick = {
            val bundle = Bundle().apply { putParcelable("product", it) }
            val action = R.id.action_homeFragment_to_productDetailsFragment
            findNavController().navigate(action, bundle)
        }
        binding.rvBestProductsBaseCategory.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollHorizontally(1) && dx != 0) {
                    onOfferPagingRequest()
                }
            }
        })
        binding.nestedScrollViewFragmentBase.setOnScrollChangeListener { v, _, scrollY, _, _ ->
            val contentView = binding.nestedScrollViewFragmentBase.getChildAt(0)
            if (contentView != null) {
                if (v.height + scrollY >= contentView.height) {
                    onBestProductsPagingRequest()
                }
            }
        }

    }

    private fun onOfferPagingRequest() {

    }

    private fun onBestProductsPagingRequest() {
    }

    fun showPBtoBestDealsWithOffer() {
        binding.progressBarBestDealsWithOffer.visibility = View.VISIBLE
    }

    fun hidePBtoBestDealsWithOffer() {
        binding.progressBarBestDealsWithOffer.visibility = View.GONE
    }

    fun showPBtoBestDeals() {
        binding.progressBarBestProducts.visibility = View.VISIBLE
    }

    fun hidePBtoBestDeals() {
        binding.progressBarBestProducts.visibility = View.GONE
    }

    private fun setupBestProductsWithOfferRv() {
        binding.rvBestDealWithOffer.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestProductsAdapterWithOffer
        }
    }

    private fun setupBestDealsRv() {
        binding.rvBestProductsBaseCategory.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

}