package com.martdev.chitchat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.martdev.chitchat.databinding.SignInFragmentBinding

class SignInFragment : Fragment() {

    private lateinit var binding: SignInFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_in_fragment, container, false)

        binding.signUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.signIn.setOnClickListener {
            binding.signIn.isEnabled = false
            val email = binding.email.editText?.text.toString()
            val password = binding.password.editText?.text.toString()

            MainActivity.firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        binding.signIn.isEnabled = true

                        MainActivity.firestore.collection(USERS)
                            .document(MainActivity.firebaseAuth.currentUser?.uid!!)
                            .get().addOnSuccessListener { doc ->
                                if (doc != null && doc.exists()) {
                                    //TODO connect to chat list
                                } else {
                                    Log.e("SignInFragment", "Doc does not exist")
                                }
                            }
                            .addOnFailureListener {
                                Log.e("SignInFragment", "Error: ${it.message}")
                            }
                    }
                }
                .addOnFailureListener(requireActivity()) {
                    binding.signIn.isEnabled = true

                    MainActivity.showToast("Authentication failed: ${it.message}", requireContext())
                }
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val currentUser = MainActivity.firebaseAuth.currentUser
        if (currentUser != null) {
            binding.email.editText?.setText(currentUser.email)
        }
    }
}