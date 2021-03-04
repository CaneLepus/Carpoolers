package com.example.carpoolers.ChatFunction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.carpoolers.R
import com.example.carpoolers.SwipeFunction.Adapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {
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
    }
}

class ChatLeftItem: Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
        return R.layout.chat_left_row
    }
}

class ChatRightItem: Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
    override fun getLayout(): Int {
        return R.layout.chat_right_row
        }
}