package com.example.myapplication.android

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myapplication.android.databinding.FragmentMainBinding

class MainFragment: Fragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        binding.button.setOnClickListener{
//            view?.findNavController()?.navigate(R.id.action_mainFragment_to_profileFragment)
        }
    }
}