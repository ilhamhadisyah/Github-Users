package com.example.githubusers.ui.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.example.githubusers.models.UserResponse
import com.example.githubusers.models.UserResponseItem

class UserDiffCallback : DiffUtil.ItemCallback<UserResponseItem>() {
    override fun areItemsTheSame(oldItem: UserResponseItem, newItem: UserResponseItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserResponseItem, newItem: UserResponseItem): Boolean {
        return oldItem == newItem
    }
}