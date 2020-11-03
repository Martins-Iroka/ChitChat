package com.martdev.chitchat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
const val USERS = "users"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_fragment)
    }

    companion object {
        val firebaseAuth = Firebase.auth
        val firestore = Firebase.firestore

        fun showToast(message: String, context: Context) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}