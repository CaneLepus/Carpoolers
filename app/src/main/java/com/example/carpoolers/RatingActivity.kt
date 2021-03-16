package com.example.carpoolers

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// UNUSED.... FOR NOW
class RatingActivity : AppCompatActivity() {

    val db = Firebase.firestore
    val auth = Firebase.auth
    private val users = db.collection("users")
    var arrList: ArrayList<Number> = ArrayList()
    var finalRating = 0.0


    val ratings: ArrayList<Double> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        var rating: RatingBar = findViewById(R.id.ratingBar)
        var button: Button = findViewById(R.id.button)


        Log.d("-----------------", arrList.toString())

        //rating.rating= finalRating.toFloat() //this is gonna need to get db value later

        button.setOnClickListener { test() }
    }


    fun getRatingsFromDb(uid: String, rating: Float) {

        val query = users.document(uid)

        query.get().addOnSuccessListener { document ->
            arrList = document.get("rating") as ArrayList<Number>

            for (item in arrList) {
                val it: Float = item.toFloat()
                finalRating += it
            }

            arrList.add(rating.toDouble())

            val data = hashMapOf("rating" to arrList)

            db.collection("users").document(uid)
                .set(data, SetOptions.merge())



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

    fun rateUser() {

    }

    private fun test() {


        //Toast.makeText(this, "You gave a rating of " + rating.rating + " stars", Toast.LENGTH_LONG).show()
        //Log.i(".RatingActivity","Rating given: " + rating.rating)

    }
}
