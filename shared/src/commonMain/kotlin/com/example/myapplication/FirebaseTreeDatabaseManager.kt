package com.example.myapplication

interface FirebaseTreeDatabaseManager {
    suspend fun createNewTree(tree: Tree)
}