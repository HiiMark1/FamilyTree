package com.example.myapplication.android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.myapplication.android.databinding.FragmentLoginBinding
import com.example.myapplication.android.login.FirebaseAuthManagerImpl
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authManager: FirebaseAuthManagerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authManager = FirebaseAuthManagerImpl()
        auth = Firebase.auth
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
}