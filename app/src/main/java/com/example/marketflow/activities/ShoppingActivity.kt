package com.example.marketflow.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.marketflow.R
import com.example.marketflow.databinding.ActivityShoppingBinding
import com.example.marketflow.utlities.Resource
import com.example.marketflow.viewmodel.CartViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShoppingBinding
    private val viewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navController = findNavController(R.id.fragmentContainerShoppingHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)
        lifecycleScope.launch {
            viewModel.cartProducts.collect {
                when (it) {
                    is Resource.Success -> {
                        val count = it.data?.size ?: 0
                        val bottomNavigationView: BottomNavigationView =
                            findViewById(R.id.bottomNavigationView)
                        bottomNavigationView.getOrCreateBadge(R.id.cartFragment).apply {
                            number = count
                            backgroundColor =
                                ContextCompat.getColor(applicationContext, R.color.g_blue)
                        }
                    }

                    else -> Unit
                }

            }
        }
    }
}