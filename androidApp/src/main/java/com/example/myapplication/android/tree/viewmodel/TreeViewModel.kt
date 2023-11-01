package com.example.myapplication.android.tree.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.UserInfo
import com.example.myapplication.UserTreeInfo
import com.example.myapplication.android.data.DIContainer
import kotlinx.coroutines.launch

class TreeViewModel {
    private var _userMainTreeInfo: MutableLiveData<Result<UserTreeInfo?>> = MutableLiveData()
    val userMainTreeInfo: LiveData<Result<UserTreeInfo?>> = _userMainTreeInfo

    private var _userMotherTreeInfo: MutableLiveData<Result<UserTreeInfo?>> = MutableLiveData()
    val userMotherTreeInfo: LiveData<Result<UserTreeInfo?>> = _userMotherTreeInfo

    private var _userFatherTreeInfo: MutableLiveData<Result<UserTreeInfo?>> = MutableLiveData()
    val userFatherTreeInfo: LiveData<Result<UserTreeInfo?>> = _userFatherTreeInfo

    private var _userChildTreeInfo: MutableLiveData<Result<UserTreeInfo?>> = MutableLiveData()
    val userChildTreeInfo: LiveData<Result<UserTreeInfo?>> = _userChildTreeInfo

    fun getMainUserInfo(uid: String) {
//        firebaseRealtimeDbManager = DIContainer.firebaseRealtimeDatabaseManagerImpl
//
//        viewModelScope.launch {
//            try {
//                val userInfo = firebaseRealtimeDbManager.getUserInfo(uid)
//                _userInfo.value = Result.success(userInfo)
//            } catch (e: Exception){
//                _error.value = e
//            }
//        }
    }
}