package com.example.marketflow.utlities

import android.util.Log
import android.util.Patterns

fun validationEmail(email: String): RegisterValidation {
    if (email.isEmpty()) {
        return RegisterValidation.Failed("Email can't be empty!")
    }
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return RegisterValidation.Failed("Wrong email format!")
    }
    return RegisterValidation.Success
}

fun validationPassword(password: String): RegisterValidation {
    if (password.isEmpty()) {
        Log.d("TAG1", password.toString())
        return RegisterValidation.Failed("Password can't be empty!")
    }
    if (password.length < 6) {
        Log.d("TAG1", password.length.toString())
        return RegisterValidation.Failed("Password should be at least 6 chars!")
    }
    Log.d("TAG1", password.length.toString())
    return RegisterValidation.Success
}