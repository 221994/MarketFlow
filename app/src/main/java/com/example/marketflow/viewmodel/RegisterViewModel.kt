package com.example.marketflow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.marketflow.data.User
import com.example.marketflow.utlities.Constants.USER_COLLECTION
import com.example.marketflow.utlities.RegisterFieldsState
import com.example.marketflow.utlities.RegisterValidation
import com.example.marketflow.utlities.Resource
import com.example.marketflow.utlities.validationEmail
import com.example.marketflow.utlities.validationPassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth, private val firebaseDatabase: FirebaseFirestore
) : ViewModel() {
    private val _register = MutableStateFlow<Resource<User>>(Resource.UnSpecified())
    val register: Flow<Resource<User>> = _register
    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener { it ->
                it.user?.let {
                    saveUserInformation(it.uid, user)
                }
            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
        } else {
            val registerFieldsStates =
                RegisterFieldsState(validationEmail(user.email), validationPassword(password))
            runBlocking {
                _validation.send(registerFieldsStates)
            }
        }
    }

    private fun saveUserInformation(userID: String, user: User) {
        firebaseDatabase.collection(USER_COLLECTION)
            .document(userID).set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)

            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())

            }

    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validationEmail(user.email)
        val passwordValidation = validationPassword(password)
        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success
        return shouldRegister
    }


}