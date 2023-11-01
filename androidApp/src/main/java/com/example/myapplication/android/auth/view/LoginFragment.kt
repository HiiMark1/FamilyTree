package com.example.myapplication.android.auth.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.myapplication.FirebaseAuthManager
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.R
import com.example.myapplication.android.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private var auth: FirebaseAuth = DIContainer.auth
    private var authManager: FirebaseAuthManager = DIContainer.firebaseAuthManagerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        with(binding) {
            btnSignUp.setOnClickListener {
                view?.findNavController()?.navigate(R.id.action_loginFragment_to_registrationFragment)
            }

            btnSignIn.setOnClickListener {
                var email: String = etMail.text.toString()
                var password: String = etPassword.text.toString()
                signIn(email, password)
                if (auth.currentUser!=null) {
                    view?.findNavController()?.navigate(R.id.action_loginFragment_to_profileFragment)
                }
            }
        }
    }

    private fun signIn(email: String, password: String){
        GlobalScope.launch{
            try {
                authManager.signInWithMailAndPassword(email, password)
            } catch (e: Exception) {
                showMessage(R.string.something_wrong)
            }
        }
    }

    private fun showMessage(msgId: Int) {
        Snackbar.make(
            requireView(),
            msgId,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showMessage(msgId: String) {
        Snackbar.make(
            requireView(),
            msgId,
            Snackbar.LENGTH_LONG
        ).show()
    }
}