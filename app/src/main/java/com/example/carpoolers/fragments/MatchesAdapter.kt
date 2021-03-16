package com.example.carpoolers.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carpoolers.ChatFunction.ChatLogActivity
import com.example.carpoolers.R
import com.example.carpoolers.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.chats_row.view.*
import java.io.File
import java.io.IOException
import java.util.*

class MatchesAdapter(val matches: List<User>) : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {
    val db = Firebase.firestore
    val auth = Firebase.auth
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return matches.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = matches[position]
        holder.itemView.textViewUserName.text = user.first + " " + user.second
        val list = mutableListOf<String>()
        list.add(auth.currentUser.uid)
        list.add(user.uid)

        db.collection("rooms")
            .whereIn("user1", list)
            .get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    for (document in result) {
                        if (document.get("user2").toString() == auth.currentUser.uid) {

                            document.reference.collection("messages")
                                .get()
                                .addOnSuccessListener { doc ->
                                    if (doc.isEmpty) {
                                        holder.itemView.imageViewAccept.visibility = View.VISIBLE
                                        holder.itemView.imageViewDecline.visibility = View.VISIBLE
                                        holder.itemView.isClickable = false
                                        holder.itemView.textViewLatestMessage.text = "wants to chat"
                                    } else {
                                        holder.itemView.imageViewAccept.visibility = View.INVISIBLE
                                        holder.itemView.imageViewDecline.visibility = View.INVISIBLE
                                        var latest = Date(1990)
                                        var text = ""
                                        for (message in doc) {
                                            if (message.getDate("timestamp")
                                                    ?.after(latest) == true
                                            ) {
                                                latest = message.getDate("timestamp")!!
                                                text = message.getString("text").toString()
                                            }
                                        }
                                        holder.itemView.textViewLatestMessage.text = text
                                        holder.itemView.isClickable = true
                                    }
                                }
                        } else if (document.get("user2").toString() == user.uid) {
                            document.reference.collection("messages")
                                .get()
                                .addOnSuccessListener { doc ->
                                    if (doc.isEmpty) {
                                        holder.itemView.imageViewAccept.visibility = View.INVISIBLE
                                        holder.itemView.imageViewDecline.visibility = View.INVISIBLE
                                        holder.itemView.textViewLatestMessage.text =
                                            "Waiting for accept"
                                        holder.itemView.isClickable = false
                                    } else {
                                        holder.itemView.imageViewAccept.visibility = View.INVISIBLE
                                        holder.itemView.imageViewDecline.visibility = View.INVISIBLE
                                        holder.itemView.isClickable = true
                                        var latest = Date(1990)
                                        var text = ""
                                        for (message in doc) {
                                            if (message.getDate("timestamp")
                                                    ?.after(latest) == true
                                            ) {
                                                latest = message.getDate("timestamp")!!
                                                text = message.getString("text").toString()
                                            }
                                        }
                                        holder.itemView.textViewLatestMessage.text = text
                                    }
                                }
                        }
                    }
                }
            }
        holder.itemView.setOnClickListener {
            db.collection("rooms")
                .whereIn("user1", list)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (list.contains(document.get("user2").toString())) {
                            val intent =
                                Intent(holder.itemView.context, ChatLogActivity::class.java)
                            intent.putExtra("roomID", document.reference.id)
                            holder.itemView.context.startActivity(intent)
                        }
                    }
                }

        }
        holder.itemView.imageViewAccept.setOnClickListener {
            db.collection("rooms")
                .whereIn("user1", list)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (list.contains(document.get("user2").toString())) {
                            document.reference.collection("messages")
                                .add(
                                    mapOf(
                                        Pair("text", "Chat room created"),
                                        Pair("user", auth.currentUser.uid),
                                        Pair("timestamp", Timestamp.now())
                                    )
                                )
                        }
                    }
                }
            holder.itemView.imageViewAccept.visibility = View.INVISIBLE
            holder.itemView.imageViewDecline.visibility = View.INVISIBLE
        }
        holder.itemView.imageViewDecline.setOnClickListener {
            db.collection("users")
                .document(auth.currentUser.uid)
                .get()
                .addOnSuccessListener { result ->
                    val roomsWith = result.get("roomsWith") as ArrayList<String>
                    for (entry in roomsWith) {
                        if (entry == user.uid) {
                            roomsWith.remove(entry)
                        }
                    }
                    val data: MutableMap<Any?, Any?> = HashMap()
                    data["roomsWith"] = roomsWith
                    result.reference.set(data, SetOptions.merge())
                }
            db.collection("users")
                .document(user.uid)
                .get()
                .addOnSuccessListener { result ->
                    val roomsWith = result.get("roomsWith") as ArrayList<String>
                    for (entry in roomsWith) {
                        if (entry == auth.currentUser.uid) {
                            roomsWith.remove(entry)
                        }
                    }
                    val data: MutableMap<Any?, Any?> = HashMap()
                    data["roomsWith"] = roomsWith
                    result.reference.set(data, SetOptions.merge())
                }
            db.collection("rooms")
                .whereIn("user1", list)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (list.contains(document.get("user2").toString())) {
                            document.reference.delete()
                        }
                    }
                }
        }
        val storageReference: StorageReference =
            FirebaseStorage.getInstance().reference.child(user.imgUrl)

        try {
            var file: File = File.createTempFile("test", "jpg")
            storageReference.getFile(file)
                .addOnSuccessListener {

                    var bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    holder.itemView.imageViewProfilePicture.setImageBitmap(bitmap)
                    holder.itemView.imageViewProfilePicture.visibility = View.VISIBLE
                }
        } catch (e: IOException) {
            Log.i("Debug", "Profile picture failed to load.")
        }


    }

    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
        inflater.inflate(R.layout.chats_row, parent, false)
    ) {
        private var chatTextSent: TextView? = null
        private var chatTextReceived: TextView? = null

        init {
            chatTextSent = itemView.findViewById(R.id.textViewChatSent)
            chatTextReceived = itemView.findViewById(R.id.textViewChatReceived)
        }
    }
}