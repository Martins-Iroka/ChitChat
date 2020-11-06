package com.martdev.chitchat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.martdev.chitchat.databinding.ChatListBinding

class ChatListFragment : Fragment() {

    private lateinit var binding: ChatListBinding
    private var query: Query? = null
    private lateinit var adapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_list, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()

        adapter.stopListening()
    }

    private fun initFireStore() {
        query = MainActivity.firestore.collection(USERS)
            .whereNotEqualTo("email", MainActivity.firebaseAuth.currentUser?.email)
    }

    private fun setRecyclerView() {
        query?.let {
            adapter = ChatListAdapter(it)
            binding.chatList.layoutManager = LinearLayoutManager(requireActivity())
            binding.chatList.adapter = adapter
            adapter.dataChanged = {
                if (adapter.itemCount == 0) {
                    MainActivity.showToast("Query came back empty", requireContext())
                }
            }

            adapter.clicked = {

            }
        }
    }
}