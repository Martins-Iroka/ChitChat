package com.martdev.chitchat

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.martdev.chitchat.databinding.ChatItemViewBinding
import com.martdev.chitchat.utils.FireStoreAdapter

class ChatListAdapter(mQuery: Query)
    : FireStoreAdapter<ChatListAdapter.ChatViewHolder>(mQuery){

    class ChatViewHolder(private val binding: ChatItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun binding(snapshot: DocumentSnapshot, clicked: (String) -> Unit) {
            val user = snapshot.toObject(User::class.java)

            if (user != null) {
                Glide.with(binding.profileImage.context)
                    .load(Uri.parse(user.profile_picture))
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .fallback(R.drawable.ic_baseline_person_24)
                    .into(binding.profileImage)

                binding.userName.text = user.fullName

                binding.container.setOnClickListener {
                    clicked(snapshot.id)
                }
            }

            binding.executePendingBindings()
        }

        companion object {
            fun getViewHolder(parent: ViewGroup): ChatViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ChatItemViewBinding.inflate(inflater, parent, false)

                return ChatViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder.getViewHolder(parent)
    }

    lateinit var clicked: (String) -> Unit
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.binding(getSnapshot(position)) {
            clicked(it)
        }
    }
}