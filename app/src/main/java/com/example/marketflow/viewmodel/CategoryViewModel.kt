package com.example.marketflow.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketflow.data.Category
import com.example.marketflow.data.Product
import com.example.marketflow.utlities.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel constructor(
    private val firebaseFireStore: FirebaseFirestore, private val category: Category
) : ViewModel() {
    private val _productsWithOffer =
        MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val productsWithOffer = _productsWithOffer.asStateFlow()
    private val _bestProducts =
        MutableStateFlow<Resource<List<Product>>>(Resource.UnSpecified())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        fetchProductsWithOffers()
        fetchBestProducts()
    }

    private fun fetchProductsWithOffers() {
        viewModelScope.launch {
            _productsWithOffer.emit(Resource.Loading())
        }
        firebaseFireStore.collection("Products").whereEqualTo("category", category.category)
            .whereEqualTo("hasOffer", true).get().addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _productsWithOffer.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _productsWithOffer.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchBestProducts() {
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }
        firebaseFireStore.collection("Products").whereEqualTo("category", category.category)
            .whereEqualTo("hasOffer", false).get().addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                    Log.d("TAG5", "In The View Model ${it.message.toString()}")

                }
            }
    }
}