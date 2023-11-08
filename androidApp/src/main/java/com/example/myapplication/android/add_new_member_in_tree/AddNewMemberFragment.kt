package com.example.myapplication.android.add_new_member_in_tree

import android.opengl.Visibility
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
import com.example.myapplication.UserTreeInfo
import com.example.myapplication.android.R
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.FragmentAddNewMemberBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddNewMemberFragment : Fragment(R.layout.fragment_add_new_member) {
    private lateinit var binding: FragmentAddNewMemberBinding
    private var auth: FirebaseAuth = DIContainer.auth
    private var firebaseTreeDatabaseManagerImpl: FirebaseTreeDatabaseManager =
        DIContainer.firebaseTreeDatabaseManagerImpl
    private lateinit var viewModel: AddNewMemberViewModel
    private var tree: Tree? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AddNewMemberViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentAddNewMemberBinding.bind(view)
        initObservers()
        viewModel.getTree()

        with(binding) {
            btnSave.visibility = View.INVISIBLE

            btnCancel.setOnClickListener {
                view?.findNavController()?.navigate(R.id.treeFragment)
            }

            btnSave.setOnClickListener {
                if (tree != null) {
                    var treeList = tree?.userInfoArray!!.toMutableList()
                    var user = getInfoFromView()
                    treeList.add(user)
                    val treeList1 = treeList.toList()
                    tree!!.userInfoArray = treeList1
                    var list = tree?.relationshipArray!!.toMutableList()
                    list.add(Relationship(user.uid))
                    tree!!.relationshipArray = list
                    GlobalScope.launch {
                        firebaseTreeDatabaseManagerImpl.saveTree(tree!!)
                    }
                    view?.findNavController()?.navigate(R.id.treeFragment)
                }
            }
        }
    }

    private fun getInfoFromView(): UserTreeInfo {
        with(binding) {
            var userTreeInfo = UserTreeInfo()
            userTreeInfo.name = etName.text.toString()
            userTreeInfo.surname = etSurname.text.toString()
            userTreeInfo.patronymic = etPatronymic.text.toString()
            userTreeInfo.birthLocation = etBirthCity.text.toString()
            userTreeInfo.actualLocation = etActualCity.text.toString()
            userTreeInfo.birthDate = etDate.text.toString() + "/" + etMonth.text.toString() + "/" +
                    etYear.text.toString()
            userTreeInfo.sex = etSex.text.toString()
            userTreeInfo.uid =
                DIContainer.treeDbRef.child("userInfoArray").push().key + System.currentTimeMillis()
                    .toString()
            return userTreeInfo
        }
    }

    private fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            showMessage(it.message.toString())
        }

        viewModel.tree.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { tree1 ->
                if (tree1 != null) {
                    tree = tree1
                    binding.btnSave.visibility = View.VISIBLE
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }
    }

    private fun showMessage(msg: String) {
        Snackbar.make(
            requireView(),
            msg,
            Snackbar.LENGTH_LONG
        ).show()
    }
}