package com.rubyf.autoplay

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.messaging.FirebaseMessaging

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard_process)

        fun startOnboarding(view: View) {

            val pm: PackageManager = getPackageManager()
            val result = isPackageInstalled("com.spotify.music", pm)

            if (!result) {
                val intent = Intent(this, SpotifyNotInstalled::class.java)
                startActivity(intent)
            }
            else {
                SpotifyControllerService.connect(this) {
                    val intent2 = Intent(this, PlaylistActivity::class.java)
                    startActivity(intent2)
                }
            }
        }

        val startButton = findViewById<AppCompatButton>(R.id.startOnboarding)
        startButton.setOnClickListener(::startOnboarding)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(ContentValues.TAG, "FCM token: $token")
            } else {
                Log.e(ContentValues.TAG, "Failed to get FCM token", task.exception)
            }
        }
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}