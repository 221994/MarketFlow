package com.example.marketflow.fragments.loginregister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.marketflow.R
import com.example.marketflow.activities.ShoppingActivity
import com.example.marketflow.databinding.FragmentLoginBinding
import com.example.marketflow.dialog.setupBottomSheetDialog
import com.example.marketflow.utlities.Resource
import com.example.marketflow.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
   private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvForgetPassword.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.resetPassword(email)
            }
        }
        lifecycleScope.launch {
            viewModel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
                        Snackbar.make(
                            requireView(), "Reset link was sent to your email", Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Error -> {
                        Snackbar.make(
                            requireView(), "Error ${it.message.toString()}", Snackbar.LENGTH_LONG
                        ).show()
                    }

                    else -> Unit
                }
            }
        }

        binding.tvDontHaveAnAccount.setOnClickListener {
            val action = R.id.action_loginFragment_to_registerFragment
            findNavController().navigate(action)
        }
        binding.apply {
            buttonLoginLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPasswordLogin.text.toString()
                viewModel.login(email, password)
            }
            lifecycleScope.launch {
                viewModel.login.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.buttonLoginLogin.startAnimation()
                        }

                        is Resource.Success -> {
                            binding.buttonLoginLogin.revertAnimation()
                            Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }

                        is Resource.Error -> {
                            Toast.makeText(
                                requireContext(), it.message.toString(), Toast.LENGTH_LONG
                            ).show()
                            binding.buttonLoginLogin.revertAnimation()
                        }

                        else -> Unit
                    }
                }
            }

        }
    }

}