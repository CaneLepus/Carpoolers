package com.example.carpoolers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notifications : AppCompatActivity() {
    private val CHANNEL_ID = "channelOne"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        val sendNotificationButton = findViewById<Button>(R.id.notificationButton)
        sendNotificationButton.setOnClickListener {
            sendNotification( "Test",
                "KJDNFKJ FJKD FKJS FKJDS FLAF LSD FLKS VBKL KÖWE FLKW FLK GKLS DKF DSLKV SLKG WEKG ",
                22)
        }
    }

    fun sendNotification( title: String, text: String, notificationId: Int) {
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }


    }
}