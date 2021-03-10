package com.example.carpoolers.ChatFunction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.carpoolers.R
import com.example.carpoolers.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"

        //Hämta vilken användare som är vald

        val adapter = GroupieAdapter()
        adapter.add(ChatLeftItem())
        adapter.add(ChatRightItem())
        adapter.add(ChatLeftItem())
        adapter.add(ChatRightItem())

        chatLogRecyclerView.adapter = adapter

        sendMesageButton.setOnClickListener {
            sendMessage()
        }
    }

    class ChatMessage(val text: String, val fromID: String, val toID: String)

    private fun sendMessage() {
        val text = enterMessage.text.toString()
        val fromID = FirebaseAuth.getInstance().uid
        //val toID =

        val chatMessage = ChatMessage(text)

    }
}

class ChatLeftItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_left_row
    }
}

class ChatRightItem : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_right_row
    }
}