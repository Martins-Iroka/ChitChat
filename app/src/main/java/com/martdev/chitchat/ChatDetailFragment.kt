package com.martdev.chitchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.martdev.chitchat.databinding.ChatDetailBinding

class ChatDetailFragment : Fragment() {

    private lateinit var binding: ChatDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.chat_detail, container, false)

        return binding.root
    }
}