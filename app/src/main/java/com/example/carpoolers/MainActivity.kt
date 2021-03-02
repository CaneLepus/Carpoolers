package com.example.carpoolers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.carpoolers.notificationcenter.FirebaseTokenCollector
import com.example.carpoolers.notificationcenter.Notifications

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //testing
        val fbtc: FirebaseTokenCollector = FirebaseTokenCollector()
        fbtc.getToken()

        val button = findViewById<Button>(R.id.loginActivityButton)
        button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val notificationPageButton = findViewById<Button>(R.id.notificationPageButton)
        notificationPageButton.setOnClickListener {
            val intent = Intent(this, Notifications::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
    }

}