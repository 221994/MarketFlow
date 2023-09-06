package com.example.marketflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketflow.data.Address
import com.example.marketflow.utlities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val fireBaseFireStore: FirebaseFirestore, val auth: FirebaseAuth
) : ViewModel() {
    private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.UnSpecified())
    val address = _address.asStateFlow()

    init {
        getUserAddress()
    }

    private fun getUserAddress() {
        viewModelScope.launch {
            _address.emit(Resource.Loading())
            fireBaseFireStore.collection("user").document(auth.uid!!).collection("address")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        viewModelScope.launch { _address.emit(Resource.Error(error.message.toString())) }
                        return@addSnapshotListener
                    }
                    val address = value?.toObjects(Address::class.java)
                    viewModelScope.launch { _address.emit(Resource.Success(address)) }
                }
        }
    }
}