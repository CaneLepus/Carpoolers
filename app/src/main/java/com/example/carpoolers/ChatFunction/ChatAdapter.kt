package com.example.carpoolers.ChatFunction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carpoolers.R
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.chat_left_row.view.*
import kotlinx.android.synthetic.main.list_item_chat.view.*

class ChatAdapter(val chatMessages: List<Messages>, val uid: String): RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }
    override fun getItemCount(): Int {
        return chatMessages.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        if (chatMessage.user == uid) {
            holder.itemView.textViewChatSent.text = chatMessage.text
            holder.itemView.textViewChatReceived.visibility = View.GONE
        } else {
            holder.itemView.textViewChatReceived.text = chatMessage.text
            holder.itemView.textViewChatSent.visibility = View.GONE
        }
    }
    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(
        inflater.inflate(R.layout.list_item_chat, parent, false)
    ) {
        private var chatTextSent: TextView? = null
        private var chatTextReceived: TextView? = null
        init {
            chatTextSent = itemView.findViewById(R.id.textViewChatSent)
            chatTextReceived = itemView.findViewById(R.id.textViewChatReceived)
        }
    }
}