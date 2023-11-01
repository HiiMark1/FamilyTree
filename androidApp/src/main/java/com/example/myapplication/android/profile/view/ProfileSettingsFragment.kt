package com.example.myapplication.android.profile.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.myapplication.FirebaseRealtimeDatabaseManager
import com.example.myapplication.UserInfo
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.profile.viewmodel.ProfileSettingsViewModel
import com.example.myapplication.android.R
import com.example.myapplication.android.databinding.FragmentProfileSettingsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileSettingsFragment : Fragment(R.layout.fragment_profile_settings) {
    private lateinit var binding: FragmentProfileSettingsBinding
    private var actualUserInfo = DIContainer.actualUserInfo
    private lateinit var viewModel: ProfileSettingsViewModel
    private var auth: FirebaseAuth = DIContainer.auth
    private var firebaseRealtimeDbManager: FirebaseRealtimeDatabaseManager =
        DIContainer.firebaseRealtimeDatabaseManagerImpl

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
                var userInfo1 = getInfoAboutUser()
                GlobalScope.launch {
                    try {
                        firebaseRealtimeDbManager.saveUserInfoData(userInfo1)
                    } catch (e: Exception) {
                        showMessage(e.toString())
                    }
                }
                view.findNavController()
                    .navigate(R.id.action_profileSettingsFragment_to_profileFragment)
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
        }
    }

    private fun getInfoAboutUser(): UserInfo {
        with(binding) {
            var actualUserInfo = UserInfo()
            actualUserInfo.uid = auth.currentUser!!.uid
            actualUserInfo.name = etName.text.toString()
            actualUserInfo.surname = etSurname.text.toString()
            actualUserInfo.patronymic = etPatronymic.text.toString()
            actualUserInfo.actualLocation = etActualCity.text.toString()
            actualUserInfo.birthLocation = etBirthCity.text.toString()
            actualUserInfo.sex = etSex.text.toString()
            actualUserInfo.birthDate = etDate.text.toString() + "/" + etMonth.text.toString() +
                    "/" + etYear.text.toString()
            return actualUserInfo
        }
    }

    private fun showMessage(msgId: String) {
        Snackbar.make(
            requireView(),
            msgId,
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
}