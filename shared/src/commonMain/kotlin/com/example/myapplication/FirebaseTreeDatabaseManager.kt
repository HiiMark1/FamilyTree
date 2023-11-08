package com.example.myapplication

interface FirebaseTreeDatabaseManager {
    suspend fun createNewTree(tree: Tree): Boolean

    suspend fun getTreeById(id: String): Tree?

    suspend fun saveTree(tree: Tree)

    suspend fun takeUsersInfo(id: String): List<UserTreeInfo>
}