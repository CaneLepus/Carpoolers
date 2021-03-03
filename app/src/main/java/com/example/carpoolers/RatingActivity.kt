package com.example.carpoolers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RatingActivity : AppCompatActivity() {

    val db = Firebase.firestore
    lateinit var rating: RatingBar
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        rating = findViewById(R.id.ratingBar)
        button = findViewById(R.id.button)


        rating.rating= 1.0F //this is gonna need to get db value later

        button.setOnClickListener{test()}
    }


    private fun test(){

        Toast.makeText(this, "You gave a rating of " + rating.rating + " stars", Toast.LENGTH_LONG)


    }
}
