package com.example.myapplication.android.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.UserInfo
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.R
import com.example.myapplication.android.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private const val ARG_USER_ID = "user_id"

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        if (DIContainer.auth.currentUser?.uid == null) {
            view?.findNavController()?.navigate(R.id.action_searchFragment_to_loginFragment)
        } else {
            with(binding) {
                var usersDataSnapshot = Firebase.database.getReference("user_info")

                val userListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val users = mutableListOf<UserInfo?>()

                        for (userSnapshot in dataSnapshot.children) {
                            val user = userSnapshot.getValue(UserInfo::class.java)
                            if (user != null) {
                                if (!user.isPrivacy) {
                                    users.add(user)
                                }
                            }
                        }
                        userListAdapter = UserListAdapter {
                            infoAboutUser(it)
                            userListAdapter?.submitList(users)
                        }

                        val decorator =
                            DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)

                        binding.users.run {
                            adapter = userListAdapter
                            addItemDecoration(decorator)
                        }

                        userListAdapter?.submitList(users)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("error", databaseError.message)
                    }
                }

                usersDataSnapshot.addValueEventListener(userListener)

                btnSearch.setOnClickListener {
                    updatePosts()
                }
            }
        }
    }

    private fun updatePosts() {
        with(binding) {
            var usersDataSnapshot = Firebase.database.getReference("user_info")

            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val users = mutableListOf<UserInfo?>()

                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(UserInfo::class.java)
                        if (user != null) {
                            if (user.name.toString()
                                    .contains(etName.text.toString())
                                && user.surname.toString()
                                    .contains(etSurname.text.toString())
                                && user.birthLocation.toString()
                                    .contains(etBirthLocation.text.toString())
                                && user.actualLocation.toString()
                                    .contains(etActualLocation.text.toString())
                                && !user.isPrivacy
                            ) {
                                users.add(user)
                            }
                        }
                    }
                    userListAdapter = UserListAdapter {
                        infoAboutUser(it)
                        userListAdapter?.submitList(users)
                    }

                    val decorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)

                    binding.users.run {
                        adapter = userListAdapter
                        addItemDecoration(decorator)
                    }

                    userListAdapter?.submitList(users)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("error", databaseError.message)
                    showMessage("Ошибка!")
                }
            }

            usersDataSnapshot.addValueEventListener(userListener)
        }
    }

    private fun infoAboutUser(it: String) {
        view?.findNavController()
            ?.navigate(
                R.id.action_searchFragment_to_infoAboutUserFragment,
                bundleOf(ARG_USER_ID to it)
            )
    }

    private fun showMessage(msg: String) {
        Snackbar.make(
            requireView(),
            msg,
            Snackbar.LENGTH_LONG
        ).show()
    }
}