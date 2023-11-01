package com.example.myapplication.android.data

import com.example.myapplication.FirebaseAuthManager
import com.example.myapplication.FirebaseRealtimeDatabaseManager
import com.example.myapplication.FirebaseTreeDatabaseManager
import com.example.myapplication.UserInfo
import com.example.myapplication.android.tree.data.FirebaseTreeDatabaseManagerImpl
import com.example.myapplication.android.auth.data.FirebaseAuthManagerImpl
import com.example.myapplication.android.profile.data.FirebaseRealtimeDatabaseManagerImpl
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object DIContainer {
    val auth = Firebase.auth
    val userInfoDbRef: DatabaseReference = Firebase.database.getReference("user_info")
    val treeDbRef: DatabaseReference = Firebase.database.getReference("tree")
    var actualUserInfo = UserInfo()

    val firebaseAuthManagerImpl: FirebaseAuthManager =
        FirebaseAuthManagerImpl(auth)

    val firebaseRealtimeDatabaseManagerImpl: FirebaseRealtimeDatabaseManager =
        FirebaseRealtimeDatabaseManagerImpl(auth, userInfoDbRef)

    val firebaseTreeDatabaseManagerImpl: FirebaseTreeDatabaseManager =
        FirebaseTreeDatabaseManagerImpl(auth, treeDbRef, userInfoDbRef)
}