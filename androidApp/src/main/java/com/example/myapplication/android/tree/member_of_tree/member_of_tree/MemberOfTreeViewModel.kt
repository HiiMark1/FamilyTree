package com.example.myapplication.android.tree.member_of_tree.member_of_tree

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Tree
import com.example.myapplication.android.data.DIContainer
import kotlinx.coroutines.launch

class MemberOfTreeViewModel(): ViewModel() {
    private val firebaseStorageServiceImpl = DIContainer.firebaseStorageServiceImpl

    private var _tree: MutableLiveData<Result<Tree?>> = MutableLiveData()
    val tree: LiveData<Result<Tree?>> = _tree

    private var _error: MutableLiveData<Exception> = MutableLiveData()
    val error: LiveData<Exception> = _error

    private var _photoUri: MutableLiveData<Result<Uri?>> = MutableLiveData()
    val photoUri: LiveData<Result<Uri?>> = _photoUri

    fun getTree() {
        viewModelScope.launch {
            try {
                val tree = DIContainer.firebaseTreeDatabaseManagerImpl.getTreeById(DIContainer.actualUserInfo.treeId.toString())
                _tree.value = Result.success(tree)
            } catch (e: Exception) {
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