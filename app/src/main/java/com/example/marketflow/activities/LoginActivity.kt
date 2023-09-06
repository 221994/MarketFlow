package com.example.marketflow.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.marketflow.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onBackPressed() {
        // This will close the app when back button is pressed from LoginRegisterActivity
        finishAffinity()
    }
}