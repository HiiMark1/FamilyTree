package com.example.myapplication.android.auth.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.myapplication.FirebaseAuthManager
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.R
import com.example.myapplication.android.databinding.FragmentRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authManager: FirebaseAuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authManager = DIContainer.firebaseAuthManagerImpl
        binding = FragmentRegistrationBinding.bind(view)

        with(binding) {
            btnSignUp.setOnClickListener {
                val email = etMail.text.toString()
                val password = etPassword.text.toString()
                if (checkEditTexts(email, password)) {
                    signUp(email, password)
                }
            }

            btnBackToLogin.setOnClickListener {
                view?.findNavController()
                    ?.navigate(R.id.action_registrationFragment_to_loginFragment)
            }
        }
    }

    private fun signUp(email: String, password: String) {
        createUser(email, password)
        if (auth.currentUser != null) {
            view?.findNavController()?.navigate(R.id.action_registrationFragment_to_loginFragment)
        }
    }

    private fun createUser(email: String, password: String) {
        GlobalScope.launch {
            try {
                authManager.signUpWithMailAndPassword(
                    email,
                    password
                )
            } catch (e: Exception) {
                showMessage(e.toString())
            }
        }
    }

    private fun checkEditTexts(email: String, password: String): Boolean {
        with(binding) {
            if (email == "") {
                showMessage(R.string.empty_login)
                return false
            }
            if (password == "") {
                showMessage(R.string.empty_password)
                return false
            }
            if (password.length < 8 || password.length > 30) {
                showMessage(R.string.password_length_error)
                return false
            }
            if (email.length < 8 || !email.contains("@") || !email.contains(".")) {
                showMessage(R.string.not_correct_mail_error)
                return false
            }
            if (password != etConfPassword.text.toString()) {
                showMessage(R.string.not_same_pass)
                return false
            }
            return true
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