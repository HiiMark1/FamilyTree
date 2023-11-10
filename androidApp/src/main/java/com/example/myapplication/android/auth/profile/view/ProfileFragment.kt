package com.example.myapplication.android.auth.profile.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.myapplication.FirebaseAuthManager
import com.example.myapplication.android.R
import com.example.myapplication.android.auth.profile.viewmodel.ProfileViewModel
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private var authManager: FirebaseAuthManager = DIContainer.firebaseAuthManagerImpl
    private var auth: FirebaseAuth = DIContainer.auth
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        initObservers()

        if (auth.currentUser == null) {
            view.findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        } else {
            viewModel.getUserInfo(auth.currentUser!!.uid)

            with(binding) {

                btnSignOut.setOnClickListener {
                    GlobalScope.launch {
                        try {
                            authManager.signOut()
                            view.findNavController()
                                .navigate(R.id.action_profileFragment_to_loginFragment)
                        } catch (e: Exception) {
                            Log.e("e", e.toString())
                        }
                    }
                }
                btnEditInfo.setOnClickListener {
                    view.findNavController()
                        .navigate(R.id.action_profileFragment_to_profileSettingsFragment)
                }
            }
        }
    }

    private fun setInfoAboutUser(userInfo: com.example.myapplication.UserInfo) {
        with(binding) {
            if (userInfo.name!=null) {
                tvName.text = userInfo.name
            }
            if(userInfo.surname!=null){
                tvSurname.text = userInfo.surname
            }

            if(userInfo.patronymic!=null){
                tvPatronymic.text = userInfo.patronymic
            }

            if(userInfo.birthDate!=null){
                tvBirthday.text = userInfo.birthDate
            }

            if(userInfo.birthLocation!=null){
                tvLocation.text = userInfo.birthLocation
            }

            if(userInfo.actualLocation!=null){
                tvActualLocation.text = userInfo.actualLocation
            }

            if(userInfo.sex!=null){
                tvSex.text = userInfo.sex
            }
        }
    }

    private fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            Log.e("e", it.message.toString())
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