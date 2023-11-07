package com.example.myapplication.android.tree.data

import com.example.myapplication.FirebaseTreeDatabaseManager
import com.example.myapplication.Tree
import com.example.myapplication.UserTreeInfo
import com.example.myapplication.android.data.DIContainer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseTreeDatabaseManagerImpl(
    private val auth: FirebaseAuth,
    private val treeDbRef: DatabaseReference,
    private val userInfoDbRef: DatabaseReference,
) : FirebaseTreeDatabaseManager {
    override suspend fun createNewTree(tree: Tree) {
        var key = treeDbRef.push().key
        if (key != null) {
            treeDbRef.child(key).setValue(tree)
            userInfoDbRef.child(auth.currentUser?.uid.toString()).child("treeId")
                .setValue(key).await()
            DIContainer.actualUserInfo.treeId = key
        }
    }

    override suspend fun getTreeById(id: String): Tree? {
        return treeDbRef.child(id).get().await()
            .getValue(Tree::class.java)
    }

    override suspend fun saveTree(tree: Tree) {
        withContext(Dispatchers.IO) {
            treeDbRef.child(DIContainer.actualUserInfo.treeId.toString()).setValue(tree)
        }
    }

    override suspend fun takeUsersInfo(id: String): List<UserTreeInfo> {
        return treeDbRef.child(id).get().await()
            .getValue(Tree::class.java)?.userInfoArray ?: emptyList()
    }
}