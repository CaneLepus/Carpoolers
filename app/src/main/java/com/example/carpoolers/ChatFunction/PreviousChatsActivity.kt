package com.example.carpoolers.ChatFunction

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.example.carpoolers.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PreviousChatsActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore
    private val users = db.collection("users")
    private val query = users.document(auth.currentUser.uid)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_chats)

        val textView = findViewById<TextView>(R.id.noMessagesTv)

        supportActionBar?.title = "Messages"

        // if (no messages) {
        textView.text = "No messages!"
        // } else {
        //textView.text = ""
       // }

    }
}