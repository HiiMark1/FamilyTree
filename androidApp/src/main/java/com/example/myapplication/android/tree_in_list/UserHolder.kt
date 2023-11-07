package com.example.myapplication.android.tree_in_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.UserTreeInfo
import com.example.myapplication.android.data.DIContainer
import com.example.myapplication.android.databinding.ItemUserBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserHolder(
    private val binding: ItemUserBinding,
    private val action: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var userTreeInfo: UserTreeInfo? = null

    init {
        itemView.setOnClickListener {
            userTreeInfo?.uid?.also(action)
        }
    }

    fun bind(user: UserTreeInfo) {
        this.userTreeInfo = user
        with(binding) {
//            GlobalScope.launch {
//                Picasso.get()
//                    .load(DIContainer.avatarsStorageRef.child(user.photoUri.toString()).downloadUrl.await())
//                    .into(ivUserPhoto)
//            }
            tvName.text = user.name.toString()
            tvSurname.text = user.surname.toString()
        }
    }

    companion object {

        fun create(
            parent: ViewGroup,
            action: (String) -> Unit
        ) = UserHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), action
        )
    }
}