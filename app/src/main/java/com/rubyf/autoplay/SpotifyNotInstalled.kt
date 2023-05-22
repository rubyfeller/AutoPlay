package com.rubyf.autoplay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class SpotifyNotInstalled : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spotify_not_installed)

        fun onClickGooglePlay(view: View) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.spotify.music"))
            startActivity(intent)
        }

        fun continueAfterInstall(view: View) {
            val intent = Intent(this, PlaylistActivity::class.java)
            startActivity(intent)
        }

        val googlePlayImage = findViewById<ImageView>(R.id.google_play)
        googlePlayImage.setOnClickListener(::onClickGooglePlay)

        val continueAfterInstallButton = findViewById<AppCompatButton>(R.id.continueAfterInstall)
        continueAfterInstallButton.setOnClickListener(::continueAfterInstall)
    }
}