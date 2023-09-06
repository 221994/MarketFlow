package com.example.marketflow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marketflow.data.User
import com.example.marketflow.utlities.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fireBaseFireStore: FirebaseFirestore, private val auth: FirebaseAuth
) : ViewModel() {
    private val _user = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
    val user = _user.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch { _user.emit(Resource.Loading()) }
        fireBaseFireStore.collection("user").document(auth.uid!!)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    viewModelScope.launch { _user.emit(Resource.Error(error.message.toString())) }
                } else {
                    val user = value?.toObject(User::class.java)
                    user?.let {
                        viewModelScope.launch { _user.emit(Resource.Success(user)) }
                    }
                }
            }
    }

    fun logout() {
        auth.signOut()
    }
}