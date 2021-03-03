package com.example.carpoolers

import android.content.Intent
import android.media.Rating
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.carpoolers.SwipeFunction.SwipeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //testing
        val fbtc: NotificationHandler = NotificationHandler()
        fbtc.getToken()

        val button = findViewById<Button>(R.id.loginActivityButton)
        button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val swipeButton = findViewById<Button>(R.id.swipeButton)
        swipeButton.setOnClickListener {
            val intent = Intent(this, SwipeActivity::class.java)
            startActivity(intent)
        }

        val ratingButton = findViewById<Button>(R.id.ratingButton)
        ratingButton.setOnClickListener{
            val intent = Intent(this, RatingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
    }

}