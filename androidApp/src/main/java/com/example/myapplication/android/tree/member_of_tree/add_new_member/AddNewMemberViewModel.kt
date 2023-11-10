package com.example.myapplication.android.tree.member_of_tree.add_new_member

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Tree
import com.example.myapplication.android.data.DIContainer
import kotlinx.coroutines.launch

class AddNewMemberViewModel: ViewModel() {
    private var _tree: MutableLiveData<Result<Tree?>> = MutableLiveData()
    val tree: LiveData<Result<Tree?>> = _tree

    private var _error: MutableLiveData<Exception> = MutableLiveData()
    val error: LiveData<Exception> = _error

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
}