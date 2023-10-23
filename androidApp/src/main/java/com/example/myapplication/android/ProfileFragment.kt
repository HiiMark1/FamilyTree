package com.example.myapplication.android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.myapplication.android.databinding.FragmentProfileBinding
import com.example.myapplication.android.login.FirebaseAuthManagerImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment: Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var authManager: FirebaseAuthManagerImpl
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        authManager = FirebaseAuthManagerImpl()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        with(binding) {
            btnExit.setOnClickListener {
                GlobalScope.launch {
                    authManager.signOut()
                }
                if (auth.currentUser==null){
                    view?.findNavController()?.navigate(R.id.action_profileFragment_to_loginFragment)
                }
            }
        }
    }
}