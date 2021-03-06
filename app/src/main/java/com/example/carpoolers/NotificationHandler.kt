package com.example.carpoolers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Tasks
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Composed by Shmonn<3
 */
class NotificationHandler: FirebaseMessagingService() {
    private val channelID = "channelMAIN"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(channelID)

    }
    override fun onNewToken(token: String) {
        Log.d("NEW TOKEN", "Refreshed token: $token")
    }

    // to get the token
    fun getToken(): String {
        var returnToken = "fcm key not implemented"
        val snapshot = FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("NEW TOKEN FAILED", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            Log.d("CURRENT TOKEN", "$token")
        })
        Thread.sleep(2000)
        if(snapshot.isComplete){
            returnToken = "${snapshot.result}"
        }

        return returnToken

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("REMOTE MESSAGE", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("MESSAGE DATA", "Message data payload: ${remoteMessage.data}")

        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("MESSAGE BODY", "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        sendLocalNotification(remoteMessage.notification?.body!!, 666)

    }

    private fun createNotificationChannel(CHANNEL_ID: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "main channel"
            val descriptionText = "channel used for testing via firebase atm"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendLocalNotification(text: String, notificationId: Int) {
        // send notification
        // Notification ID should always be unique
        var builder = NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.notifications_icon)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle()
                        .bigText(text))
                .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }
}