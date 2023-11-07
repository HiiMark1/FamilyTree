package com.example.myapplication.android.tree.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.myapplication.FirebaseRealtimeDatabaseManager
import com.example.myapplication.android.R
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.FragmentInfoAboutTreeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class InfoAboutTreeFragment : Fragment(R.layout.fragment_info_about_tree) {
    private lateinit var binding: FragmentInfoAboutTreeBinding
    private var firebaseRealtimeDatabaseManagerImpl: FirebaseRealtimeDatabaseManager =
        DIContainer.firebaseRealtimeDatabaseManagerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentInfoAboutTreeBinding.bind(view)
        with(binding) {
            tvCode.setText(DIContainer.actualUserInfo.treeId.toString())

            btnLeave.setOnClickListener {
                DIContainer.actualUserInfo.treeId = null
                GlobalScope.launch {
                    firebaseRealtimeDatabaseManagerImpl.saveUserInfoData(DIContainer.actualUserInfo)
                }
                view?.findNavController()?.navigate(R.id.treeFragment)
            }

            btnGetTreeCode.setOnClickListener {
                val clipboardManager = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", DIContainer.actualUserInfo.treeId.toString())
                clipboardManager.setPrimaryClip(clipData)

                showMessage("Код скопирован!")
            }
        }
    }

    private fun showMessage(msgId: String) {
        Snackbar.make(
            requireView(),
            msgId,
            Snackbar.LENGTH_LONG
        ).show()
    }
}