package com.example.myapplication.android.search

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.UserInfo

class UserListAdapter(
    private val action: (String) -> Unit,
) : ListAdapter<UserInfo, UserHolder>(UserDiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserHolder = UserHolder.create(parent, action)

    override fun onBindViewHolder(holder: UserHolder, position: Int) =
        holder.bind(getItem(position))

    override fun submitList(list: MutableList<UserInfo?>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}