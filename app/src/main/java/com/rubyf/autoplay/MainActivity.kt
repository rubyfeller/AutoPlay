package com.rubyf.autoplay

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rubyf.autoplay.SpotifyControllerService.resume

class MainActivity : AppCompatActivity() {
    private var appInBackground = false
    var driveMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        // If app was not in background, start playing selected playlist. If the app is reopening, resume playback
        if (!appInBackground) {
        playPlaylist()
        } else {
            resume()
            val playButton = findViewById<ImageView>(R.id.Pause)
            playButton.setImageResource(R.drawable.pause_48px)

        }

        // Initialize listeners
        setSkipPreviousClickListener()
        setPauseClickListener()
        setSkipNextClickListener()
        subscribeToPlayerState()

    }

    private fun playPlaylist() {
        val playlistUri = intent.getStringExtra("playlist_uri")
        if (playlistUri != null) {
            SpotifyControllerService.playPlaylist(playlistUri)
        }
    }

    private fun subscribeToPlayerState() {
            SpotifyControllerService.getTrack { track ->
                val songText = findViewById<TextView>(R.id.songText)
                songText.text = track.name
        }
    }

    private fun setSkipPreviousClickListener() {
        val skipPrevious = findViewById<ImageView>(R.id.skipPrevious)
        skipPrevious.setOnClickListener {
            SpotifyControllerService.skipPrevious()
            val pause = findViewById<ImageView>(R.id.Pause)
            pause.setImageResource(R.drawable.pause_48px)
        }
    }

    private fun setPauseClickListener() {
        var isPlaying = true
        val pause = findViewById<ImageView>(R.id.Pause)
        pause.setOnClickListener {
            if (isPlaying) {
                SpotifyControllerService.pause()
                pause.setImageResource(R.drawable.play_arrow_black_24dp)
            } else {
                resume()
                pause.setImageResource(R.drawable.pause_48px)
            }
            isPlaying = !isPlaying
        }
    }

    private fun setSkipNextClickListener() {
        val skipNext = findViewById<ImageView>(R.id.skipNext)
        skipNext.setOnClickListener {
            SpotifyControllerService.skipNext()
            val pause = findViewById<ImageView>(R.id.Pause)
            pause.setImageResource(R.drawable.pause_48px)
        }
    }

    override fun onStop() {
        super.onStop()
            appInBackground = true
            SpotifyControllerService.pause()
            val playButton = findViewById<ImageView>(R.id.Pause)
            playButton.setImageResource(R.drawable.play_arrow_black_24dp)
        }
     }