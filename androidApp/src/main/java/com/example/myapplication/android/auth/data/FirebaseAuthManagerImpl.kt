package com.example.myapplication.android.auth.data

import com.example.myapplication.FirebaseAuthManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirebaseAuthManagerImpl(
    private val auth: FirebaseAuth
) : FirebaseAuthManager {

    override suspend fun signInWithMailAndPassword(email: String, password: String) {
        withContext(Dispatchers.Main) {
            auth.signInWithEmailAndPassword(email, password)
        }
    }

    override suspend fun signUpWithMailAndPassword(email: String, password: String){
        withContext(Dispatchers.Main) {
            auth.createUserWithEmailAndPassword(email, password)
        }
    }

    override suspend fun signOut() {
        withContext(Dispatchers.Main) {
            auth.signOut()
        }
    }
}