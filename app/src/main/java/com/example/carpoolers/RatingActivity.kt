package com.example.carpoolers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

// UNUSED.... FOR NOW
class RatingActivity : AppCompatActivity() {

    val db = Firebase.firestore
    val auth = Firebase.auth
    private val users = db.collection("users")
    var arrList: ArrayList<Number> = ArrayList()
    var finalRating = 0.0
    lateinit var ratingBar : RatingBar
    lateinit var numberOfRatings : TextView


    val ratings: ArrayList<Double> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        ratingBar = findViewById(R.id.ratingBar)
        var button: Button = findViewById(R.id.button)
        var toUser : String? = intent.getStringExtra("user")
        numberOfRatings = findViewById(R.id.tvNumberOfRatings)


        Log.d("-----------------", arrList.toString())

        if (toUser != null) {
            getRating(toUser)
        }

        button.setOnClickListener {
            if (toUser != null) {
                var rating = ratingBar.rating
                Log.i("RatingActivityTAG", "This is the rating you are giving " + rating)
                setRating(toUser, rating)
                ratingBar.setIsIndicator(true)
            }
        }
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
        Thread.sleep(1000)

    }


    private fun test() {


        //Toast.makeText(this, "You gave a rating of " + rating.rating + " stars", Toast.LENGTH_LONG).show()
        //Log.i(".RatingActivity","Rating given: " + rating.rating)

    }


    fun setRating(uid: String, rating: Float) {

        val query = users.document(uid)

        query.get().addOnSuccessListener { document ->
            arrList = document.get("rating") as ArrayList<Number>


            arrList.add(rating)

            val data = hashMapOf("rating" to arrList)

            db.collection("users").document(uid)
                .set(data, SetOptions.merge())

            Toast.makeText(
                applicationContext, "Rating given, thank you! Rating: $rating",
                Toast.LENGTH_SHORT
            ).show()

            getRating(uid)

        }.addOnFailureListener {
            Toast.makeText(
                applicationContext, "Rating could not be initialized",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun getRating(uid: String) {
        val query = users.document(uid)
        var rating = 0.0f
        var noOfRatings = 0

        query.get().addOnSuccessListener { document ->

            val ratings =
                document["rating"] as java.util.ArrayList<Number>?

            for (item in ratings!!) {
                val it: Float = item.toFloat()
                noOfRatings++
                rating += it
            }
            rating /= ratings.size

            ratingBar.rating = rating
            numberOfRatings.text = """This user has $noOfRatings ratings"""



        }.addOnFailureListener {
            Toast.makeText(
                applicationContext, "Ratings could not be loaded.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
