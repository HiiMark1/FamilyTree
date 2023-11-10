package com.example.myapplication.android.tree.tree

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Tree
import com.example.myapplication.android.data.DIContainer
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TreeViewModel : ViewModel() {
    private val firebaseStorageServiceImpl = DIContainer.firebaseStorageServiceImpl
    private val firebaseTreeDatabaseManagerImpl = DIContainer.firebaseTreeDatabaseManagerImpl

    private var _tree: MutableLiveData<Result<Tree?>> = MutableLiveData()
    val tree: LiveData<Result<Tree?>> = _tree

    private var _mainUri: MutableLiveData<Result<Uri?>> = MutableLiveData()
    val mainUri: LiveData<Result<Uri?>> = _mainUri

    private var _motherUri: MutableLiveData<Result<Uri?>> = MutableLiveData()
    val motherUri: LiveData<Result<Uri?>> = _motherUri

    private var _fatherUri: MutableLiveData<Result<Uri?>> = MutableLiveData()
    val fatherUri: LiveData<Result<Uri?>> = _fatherUri

    private var _childUri: MutableLiveData<Result<Uri?>> = MutableLiveData()
    val childUri: LiveData<Result<Uri?>> = _childUri

    private var _error: MutableLiveData<Exception> = MutableLiveData()
    val error: LiveData<Exception> = _error

    private var _isCompleted: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val isCompleted: LiveData<Result<Boolean>> = _isCompleted

    fun createOwnTree(tree: Tree) {
        viewModelScope.launch {
            try {
                var isCompleted = firebaseTreeDatabaseManagerImpl.createNewTree(tree)
                _isCompleted.value = Result.success(isCompleted)
            } catch (ex: Exception) {
                _isCompleted.value = Result.failure(ex)
                _error.value = ex
            }
        }
    }

    fun getTree() {
        viewModelScope.launch {
            try {
                val tree =
                    firebaseTreeDatabaseManagerImpl.getTreeById(DIContainer.actualUserInfo.treeId.toString())
                _tree.value = Result.success(tree)
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }

    fun getMotherUri(url: String) {
        viewModelScope.launch {
            try {
                val downloadedUri = firebaseStorageServiceImpl.getPhotoByUrl(url)
                _motherUri.value = Result.success(downloadedUri)
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }

    fun getFatherUri(url: String) {
        viewModelScope.launch {
            try {
                val downloadedUri = firebaseStorageServiceImpl.getPhotoByUrl(url)
                _fatherUri.value = Result.success(downloadedUri)
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }

    fun getMainUri(url: String) {
        viewModelScope.launch {
            try {
                val downloadedUri = firebaseStorageServiceImpl.getPhotoByUrl(url)
                _mainUri.value = Result.success(downloadedUri)
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }

    fun getChildUri(url: String) {
        viewModelScope.launch {
            try {
                val downloadedUri = firebaseStorageServiceImpl.getPhotoByUrl(url)
                _childUri.value = Result.success(downloadedUri)
            } catch (e: Exception) {
                _error.value = e
            }
        }
    }
}