package com.example.myapplication.android.tree.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.myapplication.FirebaseRealtimeDatabaseManager
import com.example.myapplication.FirebaseTreeDatabaseManager
import com.example.myapplication.Relationship
import com.example.myapplication.Tree
import com.example.myapplication.UserInfo
import com.example.myapplication.UserTreeInfo
import com.example.myapplication.android.R
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.FragmentTreeBinding
import com.example.myapplication.android.tree.viewmodel.TreeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TreeFragment : Fragment(R.layout.fragment_tree) {
    private lateinit var binding: FragmentTreeBinding
    private var auth: FirebaseAuth = DIContainer.auth
    private lateinit var viewModel: TreeViewModel
    private var firebaseTreeDatabaseManagerImpl: FirebaseTreeDatabaseManager =
        DIContainer.firebaseTreeDatabaseManagerImpl
    private var firebaseRealtimeDatabaseManagerImpl: FirebaseRealtimeDatabaseManager =
        DIContainer.firebaseRealtimeDatabaseManagerImpl
    private var uid: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TreeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTreeBinding.bind(view)

        initObservers()
        viewModel.getTree()

        if (Firebase.auth.currentUser?.uid == null) {
            view.findNavController().navigate(R.id.treeFragment)
        } else {
            if (DIContainer.actualUserInfo.treeId == null) {
                setNoViewTree()
                with(binding) {
                    btnAccept.setOnClickListener {
                        DIContainer.actualUserInfo.treeId = etEnterCode.text.toString()
                        GlobalScope.launch {
                            firebaseRealtimeDatabaseManagerImpl.saveUserInfoData(DIContainer.actualUserInfo)
                        }
                        view?.findNavController()?.navigate(R.id.treeFragment)
                    }

                    btnCreateOwnTree.setOnClickListener {
                        createOwnTree()
                        view?.findNavController()?.navigate(R.id.treeFragment)
                    }
                }
            } else {
                with(binding) {
                    btnInfoAboutTree.setOnClickListener {
                        view.findNavController()
                            .navigate(R.id.action_treeFragment_to_infoAboutTreeFragment)
                    }

                    btnChangeViewToList.setOnClickListener {
                        view.findNavController()
                            .navigate(R.id.action_treeFragment_to_treeInListFragment)
                    }

                    btnAddNewMember.setOnClickListener {
                        view.findNavController()
                            .navigate(R.id.action_treeFragment_to_addNewMemberFragment)
                    }
                }
            }
        }
    }

    private fun createOwnTree() {
        GlobalScope.launch {
            var userInfo2 = UserTreeInfo()
            userInfo2.uid = DIContainer.actualUserInfo.uid
            userInfo2.name = DIContainer.actualUserInfo.name
            userInfo2.surname = DIContainer.actualUserInfo.surname
            userInfo2.patronymic = DIContainer.actualUserInfo.patronymic
            userInfo2.birthLocation = DIContainer.actualUserInfo.birthLocation
            userInfo2.actualLocation = DIContainer.actualUserInfo.actualLocation
            userInfo2.birthDate = DIContainer.actualUserInfo.birthDate
            userInfo2.sex = DIContainer.actualUserInfo.sex
            userInfo2.photoUri = DIContainer.actualUserInfo.photoUri
            var tree = Tree()
            var userInfoList: List<UserTreeInfo> = listOf(userInfo2)
            tree.userInfoArray = userInfoList

            var relationship = Relationship(auth.currentUser?.uid.toString())
            tree.relationshipArray = listOf(relationship)

            firebaseTreeDatabaseManagerImpl.createNewTree(tree)
        }
    }

    private suspend fun getUserInfo(uid: String): UserInfo? {
        return firebaseRealtimeDatabaseManagerImpl.getUserInfo(uid)
    }

    private fun setNoViewTree() {
        with(binding) {
            tv0child.visibility = View.INVISIBLE
            tv1child1.visibility = View.INVISIBLE
            tv1child2.visibility = View.INVISIBLE
            iv0child.visibility = View.INVISIBLE
            iv1child1.visibility = View.INVISIBLE
            iv1child2.visibility = View.INVISIBLE
            ivMainUser.visibility = View.INVISIBLE
            tvMainInfo.visibility = View.INVISIBLE
            ivFather.visibility = View.INVISIBLE
            tvFather.visibility = View.INVISIBLE
            ivMother.visibility = View.INVISIBLE
            tvMother.visibility = View.INVISIBLE
            horizLine1childs.visibility = View.INVISIBLE
            horizLineParents.visibility = View.INVISIBLE
            vertLine0childs.visibility = View.INVISIBLE
            vertLine1childs.visibility = View.INVISIBLE
            vertLineParents.visibility = View.INVISIBLE
            btnChangeViewToList.visibility = View.INVISIBLE
            btnAddNewMember.visibility = View.INVISIBLE
            btnInfoAboutTree.visibility = View.INVISIBLE
            btnCreateOwnTree.visibility = View.VISIBLE
            btnAccept.visibility = View.VISIBLE
            etEnterCode.visibility = View.VISIBLE
        }
    }

    private fun setNoChildView() {
        with(binding) {
            tv0child.visibility = View.VISIBLE
            iv0child.visibility = View.VISIBLE
            vertLine0childs.visibility = View.VISIBLE
            tv1child1.visibility = View.INVISIBLE
            tv1child2.visibility = View.INVISIBLE
            iv1child1.visibility = View.INVISIBLE
            iv1child2.visibility = View.INVISIBLE
            vertLine1childs.visibility = View.INVISIBLE
            horizLine1childs.visibility = View.INVISIBLE
        }
    }

    private fun setMainUserView(user: UserTreeInfo) {
        with(binding) {
            tvMainInfo.setText(user.name + "\n" + user.surname)
            viewModel.getMainUri(user.photoUri.toString())
        }
    }

    private fun setMotherView(user: UserTreeInfo) {
        with(binding) {
            tvMother.setText(user.name + "\n" + user.surname)
            viewModel.getMotherUri(user.photoUri.toString())
        }
    }

    private fun setFatherView(user: UserTreeInfo) {
        with(binding) {
            tvFather.setText(user.name + "\n" + user.surname)
            viewModel.getFatherUri(user.photoUri.toString())
        }
    }

    private fun setChildView(user: UserTreeInfo) {
        with(binding) {
            tv0child.setText(user.name + "\n" + user.surname)
            viewModel.getChildUri(user.photoUri.toString())
        }
    }

    private fun setViews() {
        if (uid == "") {
            uid = DIContainer.actualUserInfo.uid.toString()
        }
        var user: UserTreeInfo
        for (i in 0..DIContainer.tree.relationshipArray.size - 1) {
            if (DIContainer.tree.relationshipArray[i].userid == uid) {
                var relation = DIContainer.tree.relationshipArray[i]
                for (i in 0..DIContainer.tree.userInfoArray.size - 1) {
                    if (relation.motherUid != null && DIContainer.tree.userInfoArray[i].uid == relation.motherUid) {
                        setMotherView(DIContainer.tree.userInfoArray[i])
                    }
                    if (relation.fatherUid != null && DIContainer.tree.userInfoArray[i].uid == relation.fatherUid) {
                        setFatherView(DIContainer.tree.userInfoArray[i])
                    }
                    if (relation.userid != null && DIContainer.tree.userInfoArray[i].uid == relation.userid) {
                        setMainUserView(DIContainer.tree.userInfoArray[i])
                    }
                    if (relation.childId != null && DIContainer.tree.userInfoArray[i].uid == relation.childId) {
                        setChildView(DIContainer.tree.userInfoArray[i])
                    }
                }
                break
            }
        }
    }

    private fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            Log.e("E", it.message.toString())
        }

        viewModel.tree.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { tree ->
                if (tree != null) {
                    DIContainer.tree = tree
                    setViews()
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }

        viewModel.mainUri.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { uri ->
                if (uri != null) {
                    Picasso.get().load(uri).into(binding.ivMainUser)
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }

        viewModel.motherUri.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { uri ->
                if (uri != null) {
                    Picasso.get().load(uri).into(binding.ivMother)
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }

        viewModel.fatherUri.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { uri ->
                if (uri != null) {
                    Picasso.get().load(uri).into(binding.ivFather)
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }

        viewModel.childUri.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { uri ->
                if (uri != null) {
                    Picasso.get().load(uri).into(binding.iv0child)
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }
    }
}