package com.example.myapplication.android.data

import com.example.myapplication.FirebaseRealtimeDatabaseManager
import com.example.myapplication.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseRealtimeDatabaseManagerImpl(
    private val auth: FirebaseAuth,
    private val userInfoDbRef: DatabaseReference,
) : FirebaseRealtimeDatabaseManager {

    override suspend fun saveUserInfoData(userInfo: UserInfo) {
        withContext(Dispatchers.IO) {
            userInfoDbRef.child(auth.currentUser?.uid.toString()).setValue(userInfo)
        }
    }

    override suspend fun getUserInfo(uid: String): UserInfo? {
        return userInfoDbRef.child(uid).get().await()
            .getValue(UserInfo::class.java)
    }
}