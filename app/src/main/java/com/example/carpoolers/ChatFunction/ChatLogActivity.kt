package com.example.carpoolers.ChatFunction

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carpoolers.R
import com.example.carpoolers.Rate
import com.example.carpoolers.RatingActivity
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.rpc.Help
import kotlinx.android.synthetic.main.activity_chat_log.*
import java.util.*
import kotlin.collections.ArrayList


class ChatLogActivity : AppCompatActivity() {

    private var auth: FirebaseAuth = Firebase.auth
    private val firestore = Firebase.firestore
    private lateinit var messageButton: Button
    var chatRegistration: ListenerRegistration? = null
    val chatMessages = ArrayList<Messages>()
    private var userToRate:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_chat_log)
        messageButton = findViewById(R.id.sendMesageButton)
        messageButton.setOnClickListener {
            sendMessage()
        }

        userToRate = intent.getStringExtra("toUserID").toString()


        supportActionBar?.title = "Chat Log"

        //Hämta vilken användare som är vald

        initList()
        sendMesageButton.setOnClickListener {
            sendMessage()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item!!.itemId) {
            R.id.action_rate -> {
              val intent = Intent(this,RatingActivity::class.java)
                if (userToRate != null) {
                    Log.i("TAG Chat Log Activity: ", userToRate)
                    intent.putExtra("user", userToRate)
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item!!)
        }

//respond to menu item selection
    }

    private fun initList() {
        if (auth.currentUser.uid == null)
            return

        chatLogRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ChatAdapter(chatMessages, auth.currentUser.uid)
        chatLogRecyclerView.adapter = adapter
        listenForChatMessages()
    }

    private fun sendMessage() {
        val text = enterMessage.text.toString()
        val fromID = FirebaseAuth.getInstance().uid
        val roomID = intent.getStringExtra("roomID")
        //val toID =
        if (roomID != null) {
            firestore.collection("rooms").document(roomID).collection("messages")
                .add(
                    mapOf(
                        Pair("text", text),
                        Pair("user", fromID),
                        Pair("timestamp", Timestamp.now())
                    )
                )
        }

    }

    private fun listenForChatMessages() {
        val roomId = intent.getStringExtra("roomID")
        if (roomId == null) {
            finish()
            return
        }
        chatRegistration = firestore.collection("rooms")
            .document(roomId!!)
            .collection("messages")
            .addSnapshotListener { messageSnapshot, _ ->
                if (messageSnapshot == null || messageSnapshot.isEmpty)
                    return@addSnapshotListener
                chatMessages.clear()
                for (messageDocument in messageSnapshot.documents) {
                    Log.d("TAG", messageDocument["timestamp"].toString())
                    val timeStamp = messageDocument["timestamp"] as Timestamp
                    chatMessages.add(
                        Messages(
                            messageDocument["text"] as String,
                            messageDocument["user"] as String,
                            timeStamp.toDate()
                        )
                    )
                }
                chatMessages.sortBy { it.timeStamp }
                chatLogRecyclerView.adapter?.notifyDataSetChanged()
            }

    }

    override fun onDestroy() {
        chatRegistration?.remove()
        super.onDestroy()
    }
}

