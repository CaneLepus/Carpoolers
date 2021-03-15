package com.example.carpoolers.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.carpoolers.ChatFunction.ChatAdapter
import com.example.carpoolers.R
import com.example.carpoolers.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.fragment_matches.*
import java.util.ArrayList

class MatchesFragment : Fragment() {

    private var auth: FirebaseAuth = Firebase.auth
    private var uid = auth.currentUser.uid
    private val db = Firebase.firestore
    val matches = ArrayList<User>()

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

//        chats.get().addOnSuccessListener { documents ->
//            for(item in documents){
//                if (item.getString("user1") == uid){
//                    if (item.getString("user2") != null)otherUser = item.getString("user2")!!
//                    if (item.getString("user2") == uid){
//                        otherUser = item.getString("user1")!!
//                    }
//                    Log.i("debug", ">>> User has at least one chat")
//
//                    setUpRows()
//
//                }else{
//                    Log.i("debug", ">>> User didnt have a chat")
//                }
//            }
//
//        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }
    private fun initList() {
        if (auth.currentUser.uid == null) {
            Log.d("TAG", "user not found")
            return
        }

        recyclerViewChats.layoutManager = LinearLayoutManager(context)
        val adapter = MatchesAdapter(matches)
        recyclerViewChats.adapter = adapter
        listenForMatches()
    }
    fun listenForMatches(){
        db.collection("users").document(auth.currentUser.uid)
            .addSnapshotListener { result, e ->
                if (result == null || !result.exists()){
                    return@addSnapshotListener
                }
                matches.clear()
                val roomsWith = result.get("roomsWith") as ArrayList<String>
                Log.d("TAG", "rooms with : ${roomsWith.size}")
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        Log.d("TAG", "in success listener")
                        for (document in result){
                            if (roomsWith.contains(document.id)){
                                Log.d("TAG", "matching user found")
                                val user = User(document.get("first").toString(), document.get("last").toString(), document.get("phone").toString(), document.getDouble("latitude") as Double, document.getDouble("longitude") as Double,
                                "", ArrayList(), "", "images/${document.reference.id}",document.id)
                                matches.add(user)
                            }
                        }
                        Log.d("TAG", "updating matches")
                        recyclerViewChats.adapter?.notifyDataSetChanged()
                    }
            }
        val list = ArrayList<String>()
        list.add(auth.currentUser.uid)
        for (user in matches){
            list.add(user.uid)
        }
        db.collectionGroup("messages")
            .addSnapshotListener { value, error ->
                Log.d("TAG", "update to messages received")
                recyclerViewChats.adapter?.notifyDataSetChanged()
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initList()
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