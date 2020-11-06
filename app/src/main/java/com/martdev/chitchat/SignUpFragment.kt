package com.martdev.chitchat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.martdev.chitchat.databinding.SignUpFragmentBinding
import kotlin.properties.Delegates

class SignUpFragment : Fragment() {

    private lateinit var binding: SignUpFragmentBinding
    private var uri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false)

        binding.imageFrame.setOnClickListener {
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }.also { startActivityForResult(it, 1) }
        }
        binding.signUp.setOnClickListener {
            binding.signUp.isEnabled = false
            val fullName = binding.fullName.editText?.text.toString()
            val email = binding.email.editText?.text.toString()
            val password = binding.password.editText?.text.toString()

            MainActivity.firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) {task ->
                    if (task.isSuccessful) {
                        task.savePictureToStorage(fullName, email, password)
                    }
                }
        }
        return binding.root
    }

    private fun Task<AuthResult>.savePictureToStorage(fullName: String, email: String, password: String) {
        uri?.let { uri ->
            val ref = Firebase.storage.getReference(this.result?.user?.uid!!)
                .child("profile_picture")
                .child(uri.lastPathSegment!!)
            ref.putFile(uri).continueWithTask {task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                ref.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = User(fullName, email, password, it.result.toString())
                    saveInFireStore(user)
                }
            }
        }
    }

    private fun saveInFireStore(user: User) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && requestCode == Activity.RESULT_OK) {
            if (data != null) {
                uri = data.data

                Glide.with(this).load(uri).fallback(R.drawable.profile_picture).into(binding.profileImage)
            }
        }
    }
}