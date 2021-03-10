package com.example.carpoolers.ChatFunction

import com.google.firebase.Timestamp
import java.util.*


data class Messages(val text: String, val user: String, val timeStamp: Date) {
}