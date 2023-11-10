package com.example.myapplication.android.tree.member_of_tree.media

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Tree
import com.example.myapplication.android.data.DIContainer
import kotlinx.coroutines.launch

class MediaPhotoViewModel : ViewModel() {
    private var _tree: MutableLiveData<Result<Tree?>> = MutableLiveData()
    val tree: LiveData<Result<Tree?>> = _tree

    private var _error: MutableLiveData<Exception> = MutableLiveData()
    val error: LiveData<Exception> = _error

    private var _photoUri: MutableLiveData<Result<Uri?>> = MutableLiveData()
    val photoUri: LiveData<Result<Uri?>> = _photoUri

    private var _isCompleted: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val isCompleted: LiveData<Result<Boolean>> = _isCompleted

    fun getTree() {
        viewModelScope.launch {
            try {
                val tree =
                    DIContainer.firebaseTreeDatabaseManagerImpl.getTreeById(DIContainer.actualUserInfo.treeId.toString())
                _tree.value = Result.success(tree)
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }

    fun getPhotoUri(uid: String, i: Int) {
        viewModelScope.launch {
            try {
                val downloadedUri = DIContainer.firebaseStorageServiceImpl.getPhotoUriByIdAndIdInArray(uid, i)
                _photoUri.value = Result.success(downloadedUri)
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }

    fun uploadAndGetIsCompleted(uri: Uri, uid: String, i: Int) {
        viewModelScope.launch {
            try {
                val isCompleted = DIContainer.firebaseStorageServiceImpl.uploadPhotoAndGetIsCompleted(uri, uid, i)
                _isCompleted.value = Result.success(isCompleted)
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }
}