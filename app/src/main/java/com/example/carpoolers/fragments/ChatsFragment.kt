package com.example.carpoolers.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.carpoolers.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.fragment_chats.*

class ChatsFragment : Fragment() {

    private var auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val v = view
        setUpRows()
    }

    class ChatsRow: Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        }
        override fun getLayout(): Int {
            return R.layout.chats_row
        }
    }

    private fun setUpRows() {
        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ChatsRow())
        adapter.add(ChatsRow())
        adapter.add(ChatsRow())

        recyclerViewChats.adapter = adapter
        recyclerViewChats.addItemDecoration(DividerItemDecoration(recyclerViewChats.context, DividerItemDecoration.VERTICAL))
    }
}