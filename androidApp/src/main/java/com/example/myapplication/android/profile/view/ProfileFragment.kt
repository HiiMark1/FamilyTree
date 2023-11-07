package com.example.myapplication.android.profile.view

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.myapplication.FirebaseAuthManager
import com.example.myapplication.android.R
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.FragmentProfileBinding
import com.example.myapplication.android.profile.viewmodel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserInfo
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private var authManager: FirebaseAuthManager = DIContainer.firebaseAuthManagerImpl
    private var auth: FirebaseAuth = DIContainer.auth
    private lateinit var viewModel: ProfileViewModel
    private lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        initObservers()
        viewModel.getUserInfo(auth.currentUser!!.uid)

        if (auth.currentUser == null) {
            view?.findNavController()?.navigate(R.id.action_profileFragment_to_loginFragment)
        }

        with(binding) {

            btnSignOut.setOnClickListener {
                GlobalScope.launch {
                    try {
                        authManager.signOut()
                        view?.findNavController()
                            ?.navigate(R.id.action_profileFragment_to_loginFragment)
                    } catch (e: Exception) {
                        showMessage(e.toString())
                    }
                }
            }
            btnEditInfo.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_profileFragment_to_profileSettingsFragment)
            }
        }
    }

    private fun showMessage(msg: String) {
        Snackbar.make(
            requireView(),
            msg,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun setInfoAboutUser(userInfo: com.example.myapplication.UserInfo) {
        with(binding) {
            if (DIContainer.actualUserInfo.name!=null) {
                tvName.setText(DIContainer.actualUserInfo.name)
            }
            if(DIContainer.actualUserInfo.surname!=null){
                tvSurname.setText(DIContainer.actualUserInfo.surname)
            }

            if(DIContainer.actualUserInfo.patronymic!=null){
                tvPatronymic.setText(DIContainer.actualUserInfo.patronymic)
            }

            if(DIContainer.actualUserInfo.birthDate!=null){
                tvBirthday.setText(DIContainer.actualUserInfo.birthDate)
            }

            if(DIContainer.actualUserInfo.birthLocation!=null){
                tvLocation.setText(DIContainer.actualUserInfo.birthLocation)
            }

            if(DIContainer.actualUserInfo.actualLocation!=null){
                tvActualLocation.setText(DIContainer.actualUserInfo.actualLocation)
            }

            if(DIContainer.actualUserInfo.sex!=null){
                tvSex.setText(DIContainer.actualUserInfo.sex)
            }
        }
    }

    private fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            showMessage(it.message.toString())
        }

        viewModel.userInfo.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { userInfo ->
                if (userInfo != null) {
                    DIContainer.actualUserInfo = userInfo
                    setInfoAboutUser(DIContainer.actualUserInfo)
                    if(userInfo.photoUri!=null){
                        viewModel.getAvatarUri(DIContainer.actualUserInfo.photoUri.toString())
                    }
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }

        viewModel.photoUri.observe(viewLifecycleOwner) {it ->
            it.fold(onSuccess = {uri ->
                if(uri!=null) {
                    Picasso.get().load(uri).into(binding.ivAvatar)
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }
    }
}