package com.example.myapplication.android.login

import com.example.myapplication.FirebaseAuthManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseAuthManagerImpl : FirebaseAuthManager {
    private val auth = Firebase.auth

    override suspend fun signInWithMailAndPassword(email: String, password: String) {
        withContext(Dispatchers.Main) {
            auth.signInWithEmailAndPassword(email, password)
        }
    }

    override suspend fun signUpWithMailAndPassword(email: String, password: String): String {
        withContext(Dispatchers.Main) {
            auth.createUserWithEmailAndPassword(email, password)
        }
        return "Eee"
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }


}