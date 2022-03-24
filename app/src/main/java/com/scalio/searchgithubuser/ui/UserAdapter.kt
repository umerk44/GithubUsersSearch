package com.scalio.searchgithubuser.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.scalio.searchgithubuser.core.load
import com.scalio.searchgithubuser.databinding.ItemUserBinding
import com.scalio.searchgithubuser.model.User

class UserAdapter : PagingDataAdapter<User, UserAdapter.UserViewHolder>(UserDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val userBinding = ItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(userBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) =
        holder.bind(getItem(position))

    class UserViewHolder(private val view: ItemUserBinding) : RecyclerView.ViewHolder(view.root) {
        fun bind(user: User?) {
            with(view) {
                name.text = user?.login ?: ""
                type.text = user?.type ?: ""
                dp.load(user?.url)
            }
        }
    }

    class UserDiff : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem == newItem
        }
    }
}