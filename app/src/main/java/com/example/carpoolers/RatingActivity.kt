package com.example.carpoolers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class RatingActivity : AppCompatActivity() {

    val db = Firebase.firestore
    val auth = Firebase.auth
    lateinit var rating: RatingBar
    lateinit var button: Button
    private val users = db.collection("users")
    private val query = users.document(auth.currentUser.uid)
    var arrList : ArrayList<Double> = ArrayList()
    var finalRating = 0.0


    val ratings : ArrayList<Double> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        rating = findViewById(R.id.ratingBar)
        button = findViewById(R.id.button)

        getRatingsFromDb()


        Log.d("-----------------", arrList.toString())

        //rating.rating= finalRating.toFloat() //this is gonna need to get db value later

        button.setOnClickListener{test()}
    }


    fun getRatingsFromDb() {


        query.get().addOnSuccessListener { document ->
            arrList = document.get("rating") as ArrayList<Double>

            for(item in arrList){
                finalRating += item
            }

            finalRating /= arrList.size

            rating.rating = finalRating.toFloat()

            Toast.makeText(
                    this, arrList.toString(),
                    Toast.LENGTH_SHORT
            ).show()

        }.addOnFailureListener {
            Toast.makeText(
                    this, "Rating could not be initialized",
                    Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun test(){

        Toast.makeText(this, "You gave a rating of " + rating.rating + " stars", Toast.LENGTH_LONG).show()
        Log.i(".RatingActivity","Rating given: " + rating.rating)

    }
    }
