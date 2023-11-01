package com.example.myapplication

interface FirebaseAuthManager {
    suspend fun signInWithMailAndPassword(email: String, password:String)

    suspend fun signUpWithMailAndPassword(email: String, password: String)

    suspend fun signOut()
}