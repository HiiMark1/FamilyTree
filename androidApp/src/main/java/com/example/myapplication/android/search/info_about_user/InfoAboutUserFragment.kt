package com.example.myapplication.android.search.info_about_user

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.UserInfo
import com.example.myapplication.android.R
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.FragmentInfoAboutUserBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

private const val ARG_USER_ID = "user_id"

class InfoAboutUserFragment : Fragment(R.layout.fragment_info_about_user) {
    private lateinit var binding: FragmentInfoAboutUserBinding
    private lateinit var viewModel: InfoAboutUserViewModel
    private var userId = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[InfoAboutUserViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = arguments?.getString(ARG_USER_ID).toString()
        binding = FragmentInfoAboutUserBinding.bind(view)
        initObservers()
        viewModel.getUserInfo(userId)
        showMessage(userId)

        binding.btnCopyEmail.setOnClickListener {
            val clipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", DIContainer.actualUserInfo.treeId.toString())
            clipboardManager.setPrimaryClip(clipData)

            showMessage("Email скопирован!")
        }
    }

    private fun addInfoAboutUser(user: UserInfo) {
        with(binding) {
            tvName.text = user.name.toString()
            tvSurname.text = user.surname.toString()
            tvEmail.text = user.email.toString()
        }
        viewModel.getAvatarUri(user.photoUri.toString())
    }

    private fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            Log.e("error", it.message.toString())
            showMessage("Ошибка!")
        }

        viewModel.userInfo.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { userInfo ->
                if (userInfo != null) {
                    addInfoAboutUser(userInfo)
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

    private fun showMessage(msg: String) {
        Snackbar.make(
            requireView(),
            msg,
            Snackbar.LENGTH_LONG
        ).show()
    }
}