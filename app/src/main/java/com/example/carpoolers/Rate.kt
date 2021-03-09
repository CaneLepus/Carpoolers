package com.example.carpoolers

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Rate : AppCompatActivity() {

    val db = Firebase.firestore
    val auth = Firebase.auth
    private val users = db.collection("users")
    var arrList: ArrayList<Number> = ArrayList()
    var finalRating = 0.0

    fun setRating(context : Context, uid: String, rating: Float) {

        val query = users.document(uid)

        query.get().addOnSuccessListener { document ->
            arrList = document.get("rating") as ArrayList<Number>


            arrList.add(rating)

            val data = hashMapOf("rating" to arrList)

            db.collection("users").document(uid)
                .set(data, SetOptions.merge())

            Toast.makeText(
                context, arrList.toString(),
                Toast.LENGTH_SHORT
            ).show()

            Toast.makeText(
                context, "Rating given, thank you! Rating: $rating",
                Toast.LENGTH_SHORT
            ).show()

        }.addOnFailureListener {
            Toast.makeText(
                context, "Rating could not be initialized",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getRating(context : Context, uid : String) : Float{
        val query = users.document(uid)
        var rating = 0.0f

        query.get().addOnSuccessListener { document ->

            val ratings =
                document["rating"] as java.util.ArrayList<Number>?

            for (item in ratings!!) {
                val it: Float = item.toFloat()
                rating += it
            }
            rating /= ratings.size

            Toast.makeText(
                context, "Ratings loaded",
                Toast.LENGTH_SHORT
            ).show()

        }.addOnFailureListener {
            Toast.makeText(
                context, "Ratings could not be loaded.",
                Toast.LENGTH_SHORT
            ).show()
        }

        Thread.sleep(1000)

        return rating
    }
}