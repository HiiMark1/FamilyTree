package com.example.myapplication.android.member_of_tree

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.myapplication.Tree
import com.example.myapplication.UserTreeInfo
import com.example.myapplication.android.R
import com.example.myapplication.android.databinding.FragmentMemberOfTreeBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

private const val ARG_NAME = "user_id"

class MemberOfTreeFragment : Fragment(R.layout.fragment_member_of_tree) {
    private lateinit var viewModel: MemberOfTreeViewModel
    private lateinit var binding: FragmentMemberOfTreeBinding
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MemberOfTreeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMemberOfTreeBinding.bind(view)
        userId = arguments?.getString(ARG_NAME).toString()
        initObservers()
        viewModel.getTree()

        with(binding) {
            btnEditInfo.setOnClickListener {
                view?.findNavController()
                    ?.navigate(
                        R.id.action_memberOfTreeFragment_to_editMemberOfTreeFragment,
                        bundleOf(ARG_NAME to userId)
                    )
            }

            btnTree.setOnClickListener {
                view?.findNavController()
                    ?.navigate(
                        R.id.action_memberOfTreeFragment_to_treeFragment,
                        bundleOf(ARG_NAME to userId)
                    )
            }

            btnMediaPhoto.setOnClickListener {
                view?.findNavController()
                    ?.navigate(
                        R.id.action_memberOfTreeFragment_to_mediaPhotoFragment,
                        bundleOf(ARG_NAME to userId)
                    )
            }
        }
    }

    private fun setUserInfo(tree: Tree) {
        for (i in 0..tree.userInfoArray.size - 1) {
            if (tree.userInfoArray[i].uid == userId) {
                setUserInfoView(tree.userInfoArray[i])
                viewModel.getAvatarUri(tree.userInfoArray[i].photoUri.toString())
                break
            }
        }
    }

    private fun setUserInfoView(user: UserTreeInfo) {
        with(binding) {
            if (user.name != null) {
                tvName.setText(user.name)
            }
            if (user.surname != null) {
                tvSurname.setText(user.surname)
            }

            if (user.patronymic != null) {
                tvPatronymic.setText(user.patronymic)
            }

            if (user.birthDate != null) {
                tvBirthday.setText(user.birthDate)
            }

            if (user.birthLocation != null) {
                tvLocation.setText(user.birthLocation)
            }

            if (user.actualLocation != null) {
                tvActualLocation.setText(user.actualLocation)
            }

            if (user.sex != null) {
                tvSex.setText(user.sex)
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
                    setUserInfo(tree)
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }

        viewModel.photoUri.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { uri ->
                if (uri != null) {
                    Picasso.get().load(uri).into(binding.ivAvatar)
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }
    }
}