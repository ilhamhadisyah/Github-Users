package com.example.githubusers.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubusers.R
import com.example.githubusers.databinding.UserCardBinding
import com.example.githubusers.models.UserResponseItem
import com.example.githubusers.ui.adapter.callback.UserDiffCallback
import com.example.githubusers.utils.loadUrl

class UserListAdapter(private val onClickListener: OnItemClickListener) :
    PagingDataAdapter<UserResponseItem, UserListAdapter.UserViewHolder>(UserDiffCallback()) {

    class OnItemClickListener(val clickListener: (user: UserResponseItem) -> Unit) {
        fun onItemClicked(user: UserResponseItem) = clickListener(user)
    }

    class UserViewHolder private constructor(private val binding: UserCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserResponseItem) {
            if (user.isRead!!){
                /**
                 * Overlay is used to give status on user watched the user
                 */
                binding.layerOverlay.visibility = View.VISIBLE
            }else{
                binding.layerOverlay.visibility = View.GONE
            }
            if (user.hasNotes!!){
                binding.noteIco.visibility = View.VISIBLE
            }
            binding.username.text = user.login
            binding.noteIco.isVisible = user.hasNotes!!
            binding.avatar.loadUrl(user.avatarUrl!!)
        }

        companion object {
            fun from(parent: ViewGroup): UserViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserCardBinding.inflate(layoutInflater, parent, false)
                return UserViewHolder(binding)
            }
        }
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
        holder.itemView.setOnClickListener {
            if (item != null) {
                onClickListener.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder.from(parent)
    }
}