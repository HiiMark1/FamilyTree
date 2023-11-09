package com.example.myapplication.android.search

import androidx.recyclerview.widget.DiffUtil
import com.example.myapplication.UserInfo

class UserDiffItemCallback: DiffUtil.ItemCallback<UserInfo>() {
    override fun areItemsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean = oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: UserInfo, newItem: UserInfo): Boolean = oldItem == newItem

}