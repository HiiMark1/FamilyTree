package com.example.myapplication.android.tree_in_list

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.UserTreeInfo

class UserDiffItemCallback: DiffUtil.ItemCallback<UserTreeInfo>() {
    override fun areItemsTheSame(oldItem: UserTreeInfo, newItem: UserTreeInfo): Boolean = oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: UserTreeInfo, newItem: UserTreeInfo): Boolean = oldItem == newItem

}