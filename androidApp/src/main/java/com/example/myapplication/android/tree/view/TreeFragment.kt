package com.example.myapplication.android.tree.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TreeFragment : Fragment(R.layout.fragment_tree) {
    private lateinit var binding: FragmentTreeBinding
    private var auth: FirebaseAuth = DIContainer.auth
    private var firebaseTreeDatabaseManagerImpl: FirebaseTreeDatabaseManager =
        DIContainer.firebaseTreeDatabaseManagerImpl
    private var firebaseRealtimeDatabaseManagerImpl: FirebaseRealtimeDatabaseManager =
        DIContainer.firebaseRealtimeDatabaseManagerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTreeBinding.bind(view)

        if (Firebase.auth.currentUser?.uid == null) {
            view.findNavController().navigate(R.id.action_treeFragment_to_loginFragment)
        } else {
            var user: UserInfo? = null
            GlobalScope.launch {
                user = getUserInfo(auth.currentUser?.uid.toString())
            }
            if (user?.treeId == null) {
                setNoViewTree()
                with(binding) {
                    btnAccept.setOnClickListener {

                    }

                    btnCreateOwnTree.setOnClickListener {
                        GlobalScope.launch {
                            var userInfo1 = getUserInfo(auth.currentUser?.uid.toString())
                            var userInfo2 = UserTreeInfo()
                            userInfo2.uid = userInfo1?.uid
                            userInfo2.name = userInfo1?.name
                            userInfo2.surname = userInfo1?.surname
                            userInfo2.patronymic = userInfo1?.patronymic
                            userInfo2.birthLocation = userInfo1?.birthLocation
                            userInfo2.actualLocation = userInfo1?.actualLocation
                            userInfo2.birthDate = userInfo1?.birthDate
                            userInfo2.sex = userInfo1?.sex
                            var tree = Tree()
                            var userInfoList: List<UserTreeInfo> = listOf(userInfo2)
                            tree.userInfoArray = userInfoList

                            var relationship = Relationship(auth.currentUser?.uid.toString())
                            tree.relationshipArray = listOf(relationship)

                            firebaseTreeDatabaseManagerImpl.createNewTree(tree)
                        }
                        view?.findNavController()?.navigate(R.id.treeFragment)
                    }
                }

//                var name: String? = null,
//                var surname: String? = null,
//                var patronymic: String? = null,
//                var birthLocation: String? = null,
//                var actualLocation: String? = null,
//                var birthDate: String? = null,
//                var sex: String? = null,

            } else {

            }
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
}