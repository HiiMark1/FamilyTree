package com.example.myapplication.android.member_of_tree

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Tree
import com.example.myapplication.UserTreeInfo
import com.example.myapplication.android.R
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.FragmentEditMemberOfTreeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val ARG_NAME = "user_id"

class EditMemberOfTreeFragment : Fragment(R.layout.fragment_edit_member_of_tree) {
    private lateinit var viewModel: EditMemberOfTreeViewModel
    private lateinit var binding: FragmentEditMemberOfTreeBinding
    private lateinit var userId: String
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { uploadFile(uri) }
        }
    private var idInArray: Int = 0
    private lateinit var tree1: Tree

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[EditMemberOfTreeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditMemberOfTreeBinding.bind(view)
        userId = arguments?.getString(ARG_NAME).toString()
        initObservers()
        viewModel.getTree()

        with(binding) {
            btnUploadAvatar.setOnClickListener {
                selectImageFromGallery()
            }

            btnCancel.setOnClickListener {
                activity?.onBackPressed()
            }

            btnSave.setOnClickListener {
                var treeList = tree1.userInfoArray.toMutableList()
                treeList[idInArray] = getUserInfoFromView(userId)
                Log.e("SSSS", tree1.userInfoArray[idInArray].photoUri.toString())
                tree1.userInfoArray = treeList.toList()
                GlobalScope.launch {
                    DIContainer.firebaseTreeDatabaseManagerImpl.saveTree(tree1)
                }
            }
        }
    }

    private fun getUserInfoFromView(id: String): UserTreeInfo {
        var user = UserTreeInfo()
        user.uid = id
        with(binding) {
            user.name = etName.text.toString()
            user.surname = etSurname.text.toString()
            user.patronymic = etPatronymic.text.toString()
            user.birthLocation = etBirthCity.text.toString()
            user.actualLocation = etActualCity.text.toString()
            user.birthDate = etDate.text.toString() + "/" + etMonth.text.toString() +
                    "/" + etYear.text.toString()
            user.treeId = DIContainer.actualUserInfo.treeId
            user.photoUri = user.uid
        }
        return user
    }

    private fun setUserInfo(tree: Tree) {
        for (i in 0..tree.userInfoArray.size - 1) {
            if (tree.userInfoArray[i].uid == userId) {
                setUserInfoView(tree.userInfoArray[i])
                idInArray = i
                break
            }
        }
    }

    private fun setUserInfoView(user: UserTreeInfo) {
        with(binding) {
            if (user.name != null) {
                etName.setText(user.name)
            }
            if (user.surname != null) {
                etSurname.setText(user.surname)
            }

            if (user.patronymic != null) {
                etPatronymic.setText(user.patronymic)
            }

            if (user.birthDate != null) {
                var array = user.birthDate!!.split("/")
                etDate.setText(array[0])
                etMonth.setText(array[1])
                etYear.setText(array[2])
            }

            if (user.birthLocation != null) {
                etBirthCity.setText(user.birthLocation)
            }

            if (user.actualLocation != null) {
                etActualCity.setText(user.actualLocation)
            }

            if (user.sex != null) {
                etSex.setText(user.sex)
            }
        }
    }

    private fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            Log.e("e", it.message.toString())
        }

        viewModel.tree.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { tree2 ->
                if (tree2 != null) {
                    setUserInfo(tree2)
                    tree1 = tree2
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun uploadFile(uri: Uri) {
        GlobalScope.launch {
            DIContainer.avatarsStorageRef.child(userId).putFile(uri)
                .addOnSuccessListener {
                }.await()
        }
    }
}