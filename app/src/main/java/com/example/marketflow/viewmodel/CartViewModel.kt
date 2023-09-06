package com.example.marketflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketflow.data.CartProduct
import com.example.marketflow.firebase.FireBaseCommon
import com.example.marketflow.utlities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FireBaseCommon
) : ViewModel() {
    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(
        Resource.UnSpecified()
    )
     val cartProducts = _cartProducts.asStateFlow()
    private val _deleteDialog = MutableSharedFlow<CartProduct>()
     val deleteDialog = _deleteDialog.asSharedFlow()
    private var cartProductDocuments = emptyList<DocumentSnapshot>()
    val productPrice = cartProducts.map {
        when (it) {
            is Resource.Success -> {
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }
    fun deleteCartProduct(cartProduct: CartProduct) {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            val documentsID = cartProductDocuments[index].id
            firebaseFireStore.collection("user").document(auth.uid!!).collection("cart").document(documentsID).delete()
        }
    }
    private fun calculatePrice(data: List<CartProduct>): Float {
        return data.sumByDouble { cartProduct ->
            ((cartProduct.product.price) * cartProduct.quantity).toDouble()
        }.toFloat()
    }
    init {
        getCartProducts()
    }
    private fun getCartProducts() {
        viewModelScope.launch {
            firebaseFireStore.collection("user").document(auth.uid!!).collection("cart")
                .addSnapshotListener { value, error ->
                    if (error != null || value == null) {
                        viewModelScope.launch { _cartProducts.emit(Resource.Error(error?.message.toString())) }
                    } else {
                        cartProductDocuments = value.documents
                        val cartProduct = value.toObjects(CartProduct::class.java)
                        viewModelScope.launch {
                            _cartProducts.emit(Resource.Success(cartProduct))
                        }
                    }
                }

        }
    }

    fun changeQuantity(
        cartProduct: CartProduct, commonQuantityChange: FireBaseCommon.QuantityChange
    ) {
        val index = cartProducts.value.data?.indexOf(cartProduct)

        /** index could be equal to -1 if the function name getCartProducts delay will also delay the result we expect the index the (_cartProduct)
         * and to prevent the app from crash so we make a check=
         */
        if (index != null && index != -1) {
            val documentsID = cartProductDocuments[index].id
            when (commonQuantityChange) {
                FireBaseCommon.QuantityChange.INCREASE -> {
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    increaseQuantity(documentsID)
                }

                FireBaseCommon.QuantityChange.DECREASE -> {
                    if (cartProduct.quantity == 1) {
                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
                        return
                    }
                    viewModelScope.launch { _cartProducts.emit(Resource.Loading()) }
                    decreaseQuantity(documentsID)
                }

            }
        }
    }

    private fun decreaseQuantity(documentID: String) {
        firebaseCommon.decreaseQuantity(documentID) { value, exception ->

            if (exception != null) viewModelScope.launch {
                _cartProducts.emit(Resource.Error(exception.message.toString()))
            }
        }
    }

    private fun increaseQuantity(documentID: String) {
        firebaseCommon.increaseQuantity(documentID) { value, exception ->

            if (exception != null) viewModelScope.launch {
                _cartProducts.emit(Resource.Error(exception.message.toString()))
            }
        }
    }
}