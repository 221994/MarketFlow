package com.example.marketflow.fragments.catrogries

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.marketflow.data.Category
import com.example.marketflow.utlities.Resource
import com.example.marketflow.viewmodel.CategoryViewModel
import com.example.marketflow.viewmodel.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CupboardFragment : BaseCategoryFragment() {
    @Inject
    lateinit var fireStoreFireBase: FirebaseFirestore
    private val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(
            fireStoreFireBase, Category.Cupboard
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.productsWithOffer.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showPBtoBestDealsWithOffer()
                    }

                    is Resource.Success -> {
                        bestProductsAdapterWithOffer.differ.submitList(it.data)
                        Log.d("TAG5", it.data?.size.toString())
                        hidePBtoBestDealsWithOffer()

                    }

                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
                        showPBtoBestDealsWithOffer()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showPBtoBestDeals()
                    }

                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        hidePBtoBestDeals()
                    }

                    is Resource.Error -> {
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                            .show()
                        hidePBtoBestDeals()
                    }

                    else -> Unit
                }
            }
        }

    }
}