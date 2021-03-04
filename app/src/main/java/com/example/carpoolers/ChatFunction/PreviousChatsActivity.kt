package com.example.carpoolers.ChatFunction

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.carpoolers.R

class PreviousChatsActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_chats)

        val textView = findViewById<TextView>(R.id.noMessagesTv)

        // if (no messages) {
        textView.text = "No messages!"
        // } else {
        //textView.text = ""
       // }

    }
}