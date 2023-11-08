package com.example.myapplication.android.tree_in_list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.UserTreeInfo
import com.example.myapplication.android.R
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.FragmentTreeInListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.tasks.await

private const val ARG_NAME = "user_id"
private const val ARG_CHOOSE = "is_choose"

class TreeInListFragment : Fragment(R.layout.fragment_tree_in_list) {
    private lateinit var binding: FragmentTreeInListBinding
    private var usersDbRef = DIContainer.treeDbRef
    private var firebaseTreeDatabaseManagerImpl = DIContainer.firebaseTreeDatabaseManagerImpl
    private lateinit var viewModel: TreeListViewModel
    private lateinit var users: List<UserTreeInfo?>
    private var userListAdapter: UserListAdapter? = null
    private var chooseList = "null"
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TreeListViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentTreeInListBinding.bind(view)
        chooseList = arguments?.getString(ARG_CHOOSE).toString()
        uid = arguments?.getString(ARG_NAME).toString()
        initObservers()
        viewModel.getTree()

        if (DIContainer.actualUserInfo.uid != null && DIContainer.actualUserInfo.treeId != null) {

            with(binding) {
                swipeContainer.setOnRefreshListener {
                    viewModel.getTree()
                    swipeContainer.isRefreshing = false
                }

                users.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if (!recyclerView.canScrollVertically(1) && dy != 0) {
                            viewModel.getTree()
                        }
                    }
                })

            }
        }
    }

    private fun updateUsers(users: List<UserTreeInfo?>) {
        if (chooseList != "null") {
            userListAdapter = UserListAdapter {
                chooseMember(it)
                userListAdapter?.submitList(users.toMutableList())
            }
        } else {
            userListAdapter = UserListAdapter {
                infoAboutItem(it)
                userListAdapter?.submitList(users.toMutableList())
            }
        }

        val decorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)

        binding.users.run {
            adapter = userListAdapter
            addItemDecoration(decorator)
        }

        userListAdapter?.submitList(users.toMutableList())
    }

    private fun chooseMember(it: String) {
        var idInArray = 0
        for (i in 0..DIContainer.tree.relationshipArray.size - 1) {
            if (DIContainer.tree.relationshipArray[i].userid == uid){
                idInArray = i
                break
            }
        }
        when(chooseList){
            "mother" -> {
                var list = DIContainer.tree.relationshipArray.toMutableList()
                list[idInArray].motherUid = it
                DIContainer.tree.relationshipArray = list.toList()
                DIContainer.treeDbRef.child(DIContainer.actualUserInfo.treeId.toString())
                    .setValue(DIContainer.tree)
                showMessage(it)
            }
            "father" -> {
                var list = DIContainer.tree.relationshipArray.toMutableList()
                list[idInArray].fatherUid = it
                DIContainer.tree.relationshipArray = list.toList()
                DIContainer.treeDbRef.child(DIContainer.actualUserInfo.treeId.toString())
                    .setValue(DIContainer.tree)
            }
            "child" -> {
                var list = DIContainer.tree.relationshipArray.toMutableList()
                list[idInArray].childId = it
                DIContainer.tree.relationshipArray = list.toList()
                DIContainer.treeDbRef.child(DIContainer.actualUserInfo.treeId.toString())
                    .setValue(DIContainer.tree)
            }
        }
        view?.findNavController()?.navigate(R.id.treeFragment)
    }

    private fun infoAboutItem(it: String) {
        view?.findNavController()
            ?.navigate(
                R.id.action_treeInListFragment_to_memberOfTreeFragment,
                bundleOf(ARG_NAME to it)
            )
    }

    private fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            showMessage(it.message.toString())
        }

        viewModel.tree.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { tree ->
                if (tree != null) {
                    users = tree.userInfoArray
                    updateUsers(users)
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