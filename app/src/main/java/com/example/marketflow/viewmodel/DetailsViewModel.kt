package com.example.marketflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketflow.data.CartProduct
import com.example.marketflow.firebase.FireBaseCommon
import com.example.marketflow.utlities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val fireBaseFireStore: FirebaseFirestore,
    val auth: FirebaseAuth,
    private val firebaseCommon: FireBaseCommon
) : ViewModel() {
    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.UnSpecified())
    val addToCart = _addToCart.asStateFlow()
    fun addUpdateProductInCart(cartProduct: CartProduct) {
        viewModelScope.launch { _addToCart.emit(Resource.Loading()) }
        fireBaseFireStore.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id).get().addOnSuccessListener {
                if (it.isEmpty) {// it's means it's a new product
                    addNewProduct(cartProduct)
                } else { // we check first if the product already in our cart
                    val product = it.first().toObject(CartProduct::class.java)
                    if (product == cartProduct) { // if it's the same product we will increase the quantity
                        val documentID = it.first().id
                        increaseQuantity(documentID, cartProduct)
                    } else {// if not, then we add a new product
                        addNewProduct(cartProduct)
                    }

                }
            }.addOnFailureListener {
                viewModelScope.launch { _addToCart.emit(Resource.Error(it.message.toString())) }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseCommon.addProductToCart(cartProduct) { addedProduct, exception ->
            viewModelScope.launch {
                if (exception == null) {
                    _addToCart.emit(Resource.Success(addedProduct))
                } else {
                    _addToCart.emit(Resource.Error(exception.message.toString()))
                }
            }
        }
    }

    private fun increaseQuantity(documentID: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(documentID) { _, exception ->
            viewModelScope.launch {
                if (exception == null) {
                    _addToCart.emit(Resource.Success(cartProduct))
                } else {
                    _addToCart.emit(Resource.Error(exception.message.toString()))
                }
            }
        }
    }
}