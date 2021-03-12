package com.example.carpoolers.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.carpoolers.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_chats.*

class ChatsFragment : Fragment() {

    private var auth: FirebaseAuth = Firebase.auth
    private var uid = auth.currentUser.uid
    private val db = Firebase.firestore

    private val chats = db.collection("rooms")
    private val query = chats.document()
    private val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var otherUser = ""

        chats.get().addOnSuccessListener { documents ->
            for(item in documents){
                if (item.getString("user1") == uid){
                    if (item.getString("user2") != null)otherUser = item.getString("user2")!!
                    if (item.getString("user2") == uid){
                        otherUser = item.getString("user1")!!
                    }
                    Log.i("debug", ">>> User has at least one chat")

                    setUpRows()

                }else{
                    Log.i("debug", ">>> User didnt have a chat")
                }
            }

        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val v = view




    }

    class ChatsRow: Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        }
        override fun getLayout(): Int {
            return R.layout.chats_row
        }
    }

    private fun setUpRows() {
        adapter.add(ChatsRow())

        recyclerViewChats.adapter = adapter
        recyclerViewChats.addItemDecoration(DividerItemDecoration(recyclerViewChats.context, DividerItemDecoration.VERTICAL))
    }
}