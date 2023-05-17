package com.rubyf.autoplay

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class OnboardingActivity : AppCompatActivity() {

    private val clientId = "02c259bc614941c4beea5e1e06d826ff"
    private val redirectUri = "http://localhost"
    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard_process)

        fun startOnboarding(view: View) {

//            val pm: PackageManager = getPackageManager()
//            val result = isPackageInstalled("com.spotify.music", pm)
//
//            if (!result) {
//                val intent = Intent(this, SpotifyNotInstalled::class.java)
//                startActivity(intent)
//            }
//            else {
//                val intent2 = Intent(this, MainActivity::class.java)
//                startActivity(intent2)
//            }
        }

        val startButton = findViewById<AppCompatButton>(R.id.startOnboarding)
        startButton.setOnClickListener(::startOnboarding)
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