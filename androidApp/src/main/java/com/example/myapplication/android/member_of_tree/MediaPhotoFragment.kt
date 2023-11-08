package com.example.myapplication.android.member_of_tree

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.Tree
import com.example.myapplication.android.R
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.FragmentMediaPhotoBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val ARG_NAME = "user_id"

class MediaPhotoFragment : Fragment(R.layout.fragment_media_photo) {
    private lateinit var viewModel: MediaPhotoViewModel
    private lateinit var binding: FragmentMediaPhotoBinding
    private var userId: String = "null"
    private var numHavePhotos = 0
    private var numSetPhotoInView = 0
    private lateinit var tree: Tree
    private lateinit var ivList: MutableList<ImageView>
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { uploadFile(uri) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MediaPhotoViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMediaPhotoBinding.bind(view)
        userId = arguments?.getString(ARG_NAME).toString()
        with(binding) {
            ivList = mutableListOf(
                ivPhoto1,
                ivPhoto2,
                ivPhoto3,
                ivPhoto4,
                ivPhoto5,
                ivPhoto6,
                ivPhoto7,
                ivPhoto8,
                ivPhoto9,
                ivPhoto10
            )

            btnAddMedia.setOnClickListener {
                selectImageFromGallery()
            }
        }
        initObservers()
        viewModel.getTree()


    }

    fun findUserTreeInfo(tree: Tree) {
        for (i in 0..tree.userInfoArray.size - 1) {
            if (tree.userInfoArray[i].uid == userId) {
                numHavePhotos = tree.userInfoArray[i].numOfPhoto
                binding.tvNameAndSurname.setText(tree.userInfoArray[i].name + " " + tree.userInfoArray[i].surname)
                break
            }
        }

        viewModel.photoUri.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { uri ->
                if (uri != null) {
                    Picasso.get().load(uri).into(ivList[numSetPhotoInView])
                    ivList[numSetPhotoInView].visibility = View.VISIBLE
                    numSetPhotoInView++
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }

        for (int in 1..numHavePhotos) {
            viewModel.getPhotoUri(userId, int)
        }
    }

    private fun initObservers() {
        viewModel.tree.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = { tree1 ->
                if (tree1 != null) {
                    findUserTreeInfo(tree1)
                    tree = tree1
                }
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    fun uploadFile(uri: Uri) {
        viewModel.isCompleted.observe(viewLifecycleOwner) { it ->
            it.fold(onSuccess = {
                if (it) {
                    showMessage("Фотография успешно загружена")
                    numHavePhotos++
                    for (i in 0..tree.userInfoArray.size - 1) {
                        if (tree.userInfoArray[i].uid == userId) {
                            var list = tree.userInfoArray.toMutableList()
                            list[i].numOfPhoto = numHavePhotos
                            tree.userInfoArray = list.toList()
                            GlobalScope.launch {
                                DIContainer.firebaseTreeDatabaseManagerImpl.saveTree(tree)
                            }
                        }
                    }
                } else {
                    showMessage("Ошибка!")
                }
                viewModel.isCompleted.removeObservers(viewLifecycleOwner)
            }, onFailure = {
                Log.e("e", it.message.toString())
            })
        }

        viewModel.uploadAndGetIsCompleted(uri, userId, numHavePhotos + 1)
    }

    private fun showMessage(msg: String) {
        Snackbar.make(
            requireView(),
            msg,
            Snackbar.LENGTH_LONG
        ).show()
    }
}