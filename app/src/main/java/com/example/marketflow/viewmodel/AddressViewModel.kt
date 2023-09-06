package com.example.marketflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketflow.data.Address
import com.example.marketflow.utlities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val fireBaseFireStore: FirebaseFirestore, private val auth: FirebaseAuth
) : ViewModel() {
    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.UnSpecified())
    val addNewAddress = _addNewAddress.asStateFlow()
    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()
    fun addAddress(address: Address) {
        val validateData = validateInputData(address)
        if (validateData) {
            fireBaseFireStore.collection("user").document(auth.uid!!).collection("address")
                .document().set(address).addOnSuccessListener {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Success(address)) }

                }.addOnFailureListener {
                    viewModelScope.launch { _addNewAddress.emit(Resource.Error(it.message.toString())) }
                }
        } else {
            viewModelScope.launch { _errorMessage.emit("All Fields are required.") }
        }
    }

    private fun validateInputData(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty() && address.city.trim()
            .isNotEmpty() && address.phone.trim().isNotEmpty() && address.phone.trim()
            .isNotEmpty() && address.street.trim().isNotEmpty() && address.state.trim()
            .isNotEmpty() && address.fullName.trim().isNotEmpty()
    }
}