package com.example.marketflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketflow.data.Product
import com.example.marketflow.utlities.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(private val fireBaseFireStore: FirebaseFirestore) :
    ViewModel() {
    private val pagingInfo = PagingInfo()
    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val specialProducts: StateFlow<Resource<List<Product>>> = _specialProducts
    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val bestProducts: StateFlow<Resource<List<Product>>> = _bestProducts

    private val _bestDeals = MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val bestDeals: StateFlow<Resource<List<Product>>> = _bestDeals

    init {
        fetchSpecialProductsFromFireBase()
        fetchBestProducts()
        fetchBestDeals()
    }

    private fun fetchSpecialProductsFromFireBase() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        fireBaseFireStore.collection("Products").whereEqualTo("category", "Special Products").get()
            .addOnSuccessListener { result ->
                val specialProducts = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProducts))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProducts() {
        if (!pagingInfo.isPagingEnding) {
            viewModelScope.launch {
                _bestProducts.emit(Resource.Loading())
            }
            fireBaseFireStore.collection("Products").whereEqualTo("category", "Best Products")
                .limit(pagingInfo.bestProductsPage * 10).get().addOnSuccessListener { result ->
                    val bestProducts = result.toObjects(Product::class.java)
                    pagingInfo.isPagingEnding = bestProducts == pagingInfo.oldBestProductList
                    pagingInfo.oldBestProductList = bestProducts
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Success(bestProducts))
                    }
                    // Increase pagingInfo to show the next 10 products of the list
                    pagingInfo.bestProductsPage++

                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestProducts.emit(Resource.Error(it.message.toString()))
                    }
                }
        }
    }

    private fun fetchBestDeals() {
        viewModelScope.launch {
            _bestDeals.emit(Resource.Loading())
        }

        fireBaseFireStore.collection("Products").whereEqualTo("category", "Best Deals").get()
            .addOnSuccessListener { result ->
                val bestDeals = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDeals.emit(Resource.Success(bestDeals))
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDeals.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    internal data class PagingInfo(
        var bestProductsPage: Long = 1,
        var oldBestProductList: List<Product> = emptyList(),
        var isPagingEnding: Boolean = false
    )
}