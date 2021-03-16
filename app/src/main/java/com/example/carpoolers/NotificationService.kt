package com.example.carpoolers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates


class NotificationService : Service() {
    val db = Firebase.firestore
    val auth = Firebase.auth
    var TAG = "Notifications"
    var firstLike = true
    var users = -1

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        super.onStartCommand(intent, flags, startId)
        listenForMatches()
        return START_STICKY
    }

    override fun onCreate() {
        Log.e(TAG, "onCreate")
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy")
        super.onDestroy()
    }

    fun listenForMatches() {
        if(auth.currentUser != null) {
            db.collection("users")
                .document(auth.currentUser.uid)
                .addSnapshotListener { value, error ->
                    var newUsers = ArrayList<String>()
                    if (value != null) {
                        if (value.get("roomsWith") != null) {
                                newUsers = value.get("roomsWith") as ArrayList<String>
                            Log.e(TAG, "users: ${users}  newUsers: ${newUsers.size}")
                            if (newUsers.size > users && users != -1){
                                createNotification("You got a new like!")
                            }
                            users = newUsers.size

                        }else{
                            users = 0
                            Log.e(TAG, "users: ${users}")
                        }
                    }
                    firstLike = false

                }
        }
    }


    private fun createNotification(text: String) {
        Log.e(TAG, "createNotification")
        val mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder =
            NotificationCompat.Builder(applicationContext, default_notification_channel_id)
        mBuilder.setContentTitle("Carpoolers")
        mBuilder.setContentText(text)
        mBuilder.setTicker(text)
        mBuilder.setSmallIcon(com.example.carpoolers.R.drawable.notifications_icon)
        mBuilder.setAutoCancel(true)
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainMenuActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder.setContentIntent(contentIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "NOTIFICATION_CHANNEL_NAME",
                importance
            )
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            assert(mNotificationManager != null)
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
        assert(mNotificationManager != null)
        mNotificationManager!!.notify(System.currentTimeMillis().toInt(), mBuilder.build())
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        private const val default_notification_channel_id = "default"
    }
}