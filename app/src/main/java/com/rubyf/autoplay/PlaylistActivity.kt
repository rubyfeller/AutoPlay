package com.rubyf.autoplay

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class PlaylistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        val playlist2 = findViewById<ImageView>(R.id.playlist2)
        val playlist2Uri = "spotify:playlist:37i9dQZF1DX0BcQWzuB7ZO"
        setPlaylistOnClickListener(playlist2, playlist2Uri)

        val playlist3 = findViewById<ImageView>(R.id.playlist3)
        val playlist3Uri = "spotify:playlist:6b8350vVrYPsOFluMBeCSO"
        setPlaylistOnClickListener(playlist3, playlist3Uri)

        val playlist4 = findViewById<ImageView>(R.id.playlist4)
        val playlist4Uri = "spotify:playlist:37i9dQZF1DX2fMaj5GfMh3"
        setPlaylistOnClickListener(playlist4, playlist4Uri)

        val playlist5 = findViewById<ImageView>(R.id.playlist5)
        val playlist5Uri = "spotify:playlist:37i9dQZF1DX7CfwQr5vk7g"
        setPlaylistOnClickListener(playlist5, playlist5Uri)

        val playlist6 = findViewById<ImageView>(R.id.playlist6)
        val playlist6Uri = "spotify:playlist:37i9dQZF1DX0BcQWzuB7ZO"
        setPlaylistOnClickListener(playlist6, playlist6Uri)
    }
    private fun setPlaylistOnClickListener(playlistImageView: ImageView, playlistUri: String) {
        playlistImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("playlist_uri", playlistUri)
            startActivity(intent)
        }
    }
}