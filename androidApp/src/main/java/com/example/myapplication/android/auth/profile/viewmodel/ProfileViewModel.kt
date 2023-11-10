package com.example.myapplication.android.auth.profile.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.FirebaseRealtimeDatabaseManager
import com.example.myapplication.UserInfo
import com.example.myapplication.android.data.DIContainer
import kotlinx.coroutines.launch

class ProfileViewModel: ViewModel() {
    private var firebaseRealtimeDbManager: FirebaseRealtimeDatabaseManager =
        DIContainer.firebaseRealtimeDatabaseManagerImpl

    private var firebaseStorageServiceImpl = DIContainer.firebaseStorageServiceImpl

    private var _error: MutableLiveData<Exception> = MutableLiveData()
    val error: LiveData<Exception> = _error

    private var _userInfo: MutableLiveData<Result<UserInfo?>> = MutableLiveData()
    val userInfo: LiveData<Result<UserInfo?>> = _userInfo

    private var _photoUri: MutableLiveData<Result<Uri?>> = MutableLiveData()
    val photoUri: LiveData<Result<Uri?>> = _photoUri

    fun getUserInfo(uid: String) {
        firebaseRealtimeDbManager = DIContainer.firebaseRealtimeDatabaseManagerImpl

        viewModelScope.launch {
            try {
                val userInfo = firebaseRealtimeDbManager.getUserInfo(uid)
                _userInfo.value = Result.success(userInfo)
            } catch (e: Exception){
                _error.value = e
            }
        }
    }

    fun getAvatarUri(url: String) {
        viewModelScope.launch {
            try {
                val downloadedUri = firebaseStorageServiceImpl.getPhotoByUrl(url)
                _photoUri.value = Result.success(downloadedUri)
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }
}