package com.example.myapplication.android.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.R

class SearchFragment: Fragment(R.layout.fragment_search) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(DIContainer.auth.currentUser?.uid == null){
            view?.findNavController()?.navigate(R.id.action_searchFragment_to_loginFragment)
        } else {
            //TODO
        }
    }
}