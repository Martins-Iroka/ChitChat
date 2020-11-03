package com.martdev.chitchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.martdev.chitchat.databinding.SignUpFragmentBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: SignUpFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false)

        binding.signUp.setOnClickListener {
            binding.signUp.isEnabled = false
            val fullName = binding.fullName.editText?.text.toString()
            val email = binding.email.editText?.text.toString()
            val password = binding.password.editText?.text.toString()

            MainActivity.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) {task ->
                    if (task.isSuccessful) {
                        binding.signUp.isEnabled = true
                        val user = User(fullName, email, password)
                        MainActivity.firestore.collection(USERS)
                            .document(MainActivity.firebaseAuth.currentUser?.uid!!).set(user)
                            .addOnSuccessListener {
                                MainActivity.showToast("Sign up successful", requireContext())
                                findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                            }
                            .addOnFailureListener {
                                binding.signUp.isEnabled = false
                                MainActivity.showToast("Sign up failed", requireContext())

                            }
                    }
                }
        }
        return binding.root
    }
}