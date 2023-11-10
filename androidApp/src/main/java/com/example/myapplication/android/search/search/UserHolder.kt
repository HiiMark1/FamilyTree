package com.example.myapplication.android.search.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.UserInfo
import com.example.myapplication.android.databinding.ItemUserSearchBinding

class UserHolder(
    private val binding: ItemUserSearchBinding,
    private val action: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var userInfo: UserInfo? = null

    init {
        itemView.setOnClickListener {
            userInfo?.uid?.also(action)
        }
    }

    fun bind(user: UserInfo) {
        this.userInfo = user
        with(binding) {
            tvNameAndSurname.setText(user.name.toString() + " " + user.surname.toString())
            tvCity.setText("место рожд: " + user.birthLocation.toString() + " / Нынешнее: " + user.actualLocation.toString())
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = UserHolder(
            ItemUserSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), action
        )
    }
}