package com.example.myapplication

interface FirebaseRealtimeDatabaseManager {
    suspend fun saveUserInfoData(userInfo: UserInfo)

    suspend fun getUserInfo(uid: String): UserInfo?
}