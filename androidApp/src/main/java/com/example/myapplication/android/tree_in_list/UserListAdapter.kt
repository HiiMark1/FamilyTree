package com.example.myapplication.android.tree_in_list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.UserTreeInfo

class UserListAdapter(
    private val action: (String) -> Unit,
) : ListAdapter<UserTreeInfo, UserHolder>(UserDiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserHolder = UserHolder.create(parent, action)

    override fun onBindViewHolder(holder: UserHolder, position: Int) =
        holder.bind(getItem(position))

    override fun submitList(list: MutableList<UserTreeInfo?>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}