package com.rubyf.autoplay

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val playAudio = PlayAudio()

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("Firebase messaging", "Notification received: $message")
        val notificationTitle = message.notification?.title
        if (notificationTitle == "speeding") {
            Log.d("Firebase messaging", "Notification (speeding) received: $message")
            val resourceId = R.raw.speedlimit_exceeded
            playAudio.playAudio(this, resourceId)
        }
        if (notificationTitle == "severe_weather") {
            Log.d("Firebase messaging", "Notification (severe_weather) received: $message")
            val resourceId = R.raw.severe_weather
            playAudio.playAudio(this, resourceId)
        }
        if (notificationTitle == "play_news") {
            Log.d("Firebase messaging", "Notification (play_news) received: $message")
            playAudio.playNews()
        }
    }
}