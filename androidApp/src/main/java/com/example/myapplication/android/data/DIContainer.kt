package com.example.myapplication.android.data

import com.example.myapplication.FirebaseAuthManager
import com.example.myapplication.FirebaseRealtimeDatabaseManager
import com.example.myapplication.FirebaseTreeDatabaseManager
import com.example.myapplication.Tree
import com.example.myapplication.UserInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

object DIContainer {
    val auth = Firebase.auth
    private val userInfoDbRef: DatabaseReference = Firebase.database.getReference("user_info")
    val treeDbRef: DatabaseReference = Firebase.database.getReference("tree")
    val avatarsStorageRef = FirebaseStorage.getInstance().getReference("avatars/")
    private val photosStorageRef = FirebaseStorage.getInstance().getReference("photos/")

    var actualUserInfo = UserInfo()
    var tree = Tree()

    val firebaseAuthManagerImpl: FirebaseAuthManager =
        FirebaseAuthManagerImpl(auth)

    val firebaseRealtimeDatabaseManagerImpl: FirebaseRealtimeDatabaseManager =
        FirebaseRealtimeDatabaseManagerImpl(auth, userInfoDbRef)

    val firebaseTreeDatabaseManagerImpl: FirebaseTreeDatabaseManager =
        FirebaseTreeDatabaseManagerImpl(auth, treeDbRef, userInfoDbRef)

    val firebaseStorageServiceImpl =
        FirebaseStorageServiceImpl(photosStorageRef, avatarsStorageRef)
}