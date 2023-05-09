package com.rubyf.autoplay

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track

class MainActivity : AppCompatActivity() {

    private val clientId = "02c259bc614941c4beea5e1e06d826ff"
    private val redirectUri = "http://localhost"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()

        SpotifyAppRemote.connect(this, connectionParams,
            object : Connector.ConnectionListener {
                override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                    this@MainActivity.spotifyAppRemote = spotifyAppRemote
                    Log.d("MainActivity", "Connected! Yay!")
                    connected()
                }

                override fun onFailure(throwable: Throwable) {
                    Log.e("MainActivity", throwable.message, throwable)
                    // Something went wrong when attempting to connect! Handle errors here
                }
            })
    }

    private fun connected() {
        spotifyAppRemote?.let {
            // Play a playlist
            val playlistUri = "spotify:playlist:37i9dQZEVXbK4BFAukDzj3"
            it.playerApi.play(playlistUri)
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback { playerState ->
                val track: Track = playerState.track
                Log.d("MainActivity", track.name + " by " + track.artist.name)
            }

            val skipPreviousImageView = findViewById<ImageView>(R.id.skipPrevious)
            val play = findViewById<ImageView>(R.id.Play)
            val skipNext = findViewById<ImageView>(R.id.skipNext)

            skipPreviousImageView.setOnClickListener {
                spotifyAppRemote?.playerApi?.skipPrevious()
            }

            play.setOnClickListener {
                if (isPlaying) {
                    // Pause the music
                    spotifyAppRemote?.playerApi?.pause()
                    play.setImageResource(R.drawable.play_arrow_black_24dp)
                } else {
                    // Play the music
                    spotifyAppRemote?.playerApi?.resume()
                    play.setImageResource(R.drawable.pause_48px)
                }
                isPlaying = !isPlaying
            }

            skipNext.setOnClickListener {
                spotifyAppRemote?.playerApi?.skipNext()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            it.playerApi.pause()
            isPlaying = false
            val playButton = findViewById<ImageView>(R.id.Play)
            playButton.setImageResource(R.drawable.play_arrow_black_24dp)
        }
    }
}