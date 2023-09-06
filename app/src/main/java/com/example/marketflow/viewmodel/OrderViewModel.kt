package com.example.marketflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketflow.data.order.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.marketflow.utlities.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore, private val auth: FirebaseAuth
) : ViewModel() {

    private val _order = MutableStateFlow<Resource<Order>>(Resource.UnSpecified())
    val order = _order.asStateFlow()

    fun placeOrder(order: Order) {
        viewModelScope.launch { _order.emit(Resource.Loading()) }
        // if one operator failed the whole block will failed in runBatch
        firebaseFireStore.runBatch { it ->
            // first we add order into user-orders collection
            // add the order into orders collection
            // delete the products from user cart collection
            firebaseFireStore.collection("user").document(auth.uid!!).collection("orders")
                .document().set(order)
            firebaseFireStore.collection("orders").document().set(order)
            firebaseFireStore.collection("user").document(auth.uid!!).collection("cart").get()
                .addOnSuccessListener { it ->
                    it.documents.forEach {
                        it.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch { _order.emit(Resource.Success(order)) }
        }.addOnFailureListener {
            viewModelScope.launch { _order.emit(Resource.Error(it.message.toString())) }
        }
    }

}