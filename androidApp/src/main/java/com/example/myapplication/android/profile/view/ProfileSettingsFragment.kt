package com.example.myapplication.android.profile.view

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.myapplication.FirebaseRealtimeDatabaseManager
import com.example.myapplication.UserInfo
import com.example.myapplication.android.R
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.FragmentProfileSettingsBinding
import com.example.myapplication.android.profile.viewmodel.ProfileSettingsViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileSettingsFragment : Fragment(R.layout.fragment_profile_settings) {
    private lateinit var binding: FragmentProfileSettingsBinding
    private var actualUserInfo = DIContainer.actualUserInfo
    private lateinit var viewModel: ProfileSettingsViewModel
    private var auth: FirebaseAuth = DIContainer.auth
    private var firebaseRealtimeDbManager: FirebaseRealtimeDatabaseManager =
        DIContainer.firebaseRealtimeDatabaseManagerImpl
    private var firebaseStorageRef = DIContainer.avatarsStorageRef
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { uploadFile(uri) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileSettingsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentProfileSettingsBinding.bind(view)
        initObservers()
        viewModel.getUserInfo(auth.currentUser!!.uid)

        with(binding) {

            btnCancel.setOnClickListener {
                view.findNavController()
                    .navigate(R.id.action_profileSettingsFragment_to_profileFragment)
            }

            btnSave.setOnClickListener {
                GlobalScope.launch {
                    getInfoAboutUser()
                    try {
                        firebaseRealtimeDbManager.saveUserInfoData(DIContainer.actualUserInfo)
                    } catch (e: Exception) {
                        showMessage(e.toString())
                    }
                }
                view.findNavController()
                    .navigate(R.id.action_profileSettingsFragment_to_profileFragment)
            }

            btnUploadAvatar.setOnClickListener {
                selectImageFromGallery()
            }
        }
    }

    private fun addInfoAboutUser(userInfo: UserInfo) {
        with(binding) {
            etName.setText(userInfo.name)
            etSurname.setText(userInfo.surname)
            etPatronymic.setText(userInfo.patronymic)
            etActualCity.setText(userInfo.actualLocation)
            etBirthCity.setText(userInfo.birthLocation)
            etSex.setText(userInfo.sex)
            var list = userInfo.birthDate?.split("/")
            etDate.setText(list?.get(0) ?: "xx")
            etMonth.setText(list?.get(1) ?: "xx")
            etYear.setText(list?.get(2) ?: "xxxx")
            etEmail.setText(userInfo.email)
            if(userInfo.isPrivacy){
                switchPrivacy.isChecked = true
            }
        }
    }

    private fun getInfoAboutUser() {
        with(binding) {
            DIContainer.actualUserInfo.uid = auth.currentUser!!.uid
            DIContainer.actualUserInfo.name = etName.text.toString()
            DIContainer.actualUserInfo.surname = etSurname.text.toString()
            DIContainer.actualUserInfo.patronymic = etPatronymic.text.toString()
            DIContainer.actualUserInfo.actualLocation = etActualCity.text.toString()
            DIContainer.actualUserInfo.birthLocation = etBirthCity.text.toString()
            DIContainer.actualUserInfo.sex = etSex.text.toString()
            DIContainer.actualUserInfo.birthDate = etDate.text.toString() + "/" + etMonth.text.toString() +
                    "/" + etYear.text.toString()
            DIContainer.actualUserInfo.email = etEmail.text.toString()
            DIContainer.actualUserInfo.isPrivacy = switchPrivacy.isChecked
        }
    }

    private fun showMessage(msg: String) {
        Snackbar.make(
            requireView(),
            msg,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            showMessage(it.message.toString())
        }

        viewModel.userInfo.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { userInfo ->
                if (userInfo != null) {
                    //actualUserInfo = userInfo
                    addInfoAboutUser(actualUserInfo)
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun uploadFile(uri: Uri) {
        DIContainer.actualUserInfo.photoUri = DIContainer.actualUserInfo.uid
        GlobalScope.launch {
            firebaseStorageRef.child(DIContainer.actualUserInfo.uid.toString()).putFile(uri)
                .addOnSuccessListener {
                    showMessage("Изображение успешно загружено")
                }.await()
        }
    }
}