package com.rubyf.autoplay

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard_process)

        fun startOnboarding(view: View) {
            val pm: PackageManager = getPackageManager()
            val result = isPackageInstalled("com.spotify.music", pm)

            if (!result) {
                val spotify = Intent(this, SpotifyNotInstalled::class.java)
                startActivity(spotify)
            }
            else {
                val playlist = Intent(this, PlaylistActivity::class.java)
                startActivity(playlist)
            }
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