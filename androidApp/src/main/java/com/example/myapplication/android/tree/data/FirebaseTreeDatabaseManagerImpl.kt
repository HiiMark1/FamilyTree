package com.example.myapplication.android.tree.data

import com.example.myapplication.FirebaseTreeDatabaseManager
import com.example.myapplication.Tree
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class FirebaseTreeDatabaseManagerImpl(
    private val auth: FirebaseAuth,
    private val treeDbRef: DatabaseReference,
    private val userInfoDbRef: DatabaseReference,
) : FirebaseTreeDatabaseManager {
    override suspend fun createNewTree(tree: Tree) {
        var key = treeDbRef.push().key
        if (key!=null) {
            treeDbRef.child(key).setValue(tree)
            userInfoDbRef.child(auth.currentUser?.uid.toString()).child("treeId").setValue(key)
        }
    }

    suspend fun connectToTree() {

    }

    suspend fun AddUserInfo() {

    }

    suspend fun takeUsersInfo() {
        
    }
}