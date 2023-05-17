package com.rubyf.autoplay

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class MainActivity : AppCompatActivity() {

    private val clientId = "02c259bc614941c4beea5e1e06d826ff"
    private val redirectUri = "http://localhost"
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private var isPlaying = true
    var driveMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playNews()
    }

    private fun playNews() {
        lifecycleScope.launch {
            try {
                val mp3Url = withContext(Dispatchers.IO) { getMp3UrlFromXml() }
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(mp3Url)
                mediaPlayer.prepare()
                mediaPlayer.start()
                val songText = findViewById<TextView>(R.id.songText)
                songText.text = resources.getString(R.string.anpNews)

                if (isPlaying) {
                    spotifyAppRemote?.playerApi?.pause()
                }
                // release the MediaPlayer object when the audio playback is finished and restart Spotify
                mediaPlayer.setOnCompletionListener {
                    mediaPlayer.release()
                    spotifyAppRemote?.playerApi?.resume()
                    isPlaying = true
                }
            } catch (e: Exception) {
                Log.e("playAudio_catch", e.message, e)
            }
        }
    }

    private fun getMp3UrlFromXml(): String {
        val url =
            URL("https://www.omnycontent.com/d/playlist/56ccbbb7-0ff7-4482-9d99-a88800f49f6c/a49c87f6-d567-4189-8692-a8e2009eaf86/9fea2041-fccd-4fcf-8cec-a8e2009eeca2/podcast.rss")
        val romeFeed = SyndFeedInput().build(XmlReader(url))
        val entries = romeFeed.entries
        // loop through the entries to find the MP3 URL
        for (entry in entries) {
            val enclosures = entry.enclosures
            for (enclosure in enclosures) {
                if (enclosure.type == "audio/mpeg") {
                    return enclosure.url
                }
            }
        }
        throw Exception("MP3 URL not found")
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
                }
            })
    }

    private fun connected() {
        spotifyAppRemote?.let {
            playPlaylist(it)
            subscribeToPlayerState(it)
            setSkipPreviousClickListener(it)
            setPauseClickListener()
            setSkipNextClickListener(it)
        }
    }
    private fun playPlaylist(spotifyAppRemote: SpotifyAppRemote) {
        val playlistUri = intent.getStringExtra("playlist_uri")
        spotifyAppRemote.playerApi.play(playlistUri)
    }

    private fun subscribeToPlayerState(spotifyAppRemote: SpotifyAppRemote) {
        spotifyAppRemote.playerApi.subscribeToPlayerState().setEventCallback { playerState ->
            val track: Track = playerState.track
            Log.d("MainActivity", track.name + " by " + track.artist.name)
            val songText = findViewById<TextView>(R.id.songText)
            songText.text = track.name
        }
    }

    private fun setSkipPreviousClickListener(spotifyAppRemote: SpotifyAppRemote) {
        val skipPrevious = findViewById<ImageView>(R.id.skipPrevious)
        skipPrevious.setOnClickListener {
            spotifyAppRemote.playerApi.skipPrevious()
            val pause = findViewById<ImageView>(R.id.Pause)
            pause.setImageResource(R.drawable.pause_48px)
        }
    }

    private fun setPauseClickListener() {
        var isPlaying = true
        val pause = findViewById<ImageView>(R.id.Pause)
        pause.setOnClickListener {
            if (isPlaying) {
                spotifyAppRemote?.playerApi?.pause()
                pause.setImageResource(R.drawable.play_arrow_black_24dp)
            } else {
                spotifyAppRemote?.playerApi?.resume()
                pause.setImageResource(R.drawable.pause_48px)
            }
            isPlaying = !isPlaying
        }
    }

    private fun setSkipNextClickListener(spotifyAppRemote: SpotifyAppRemote) {
        val skipNext = findViewById<ImageView>(R.id.skipNext)
        skipNext.setOnClickListener {
            spotifyAppRemote.playerApi.skipNext()
            val pause = findViewById<ImageView>(R.id.Pause)
            pause.setImageResource(R.drawable.pause_48px)
        }
    }
    override fun onStop() {
        super.onStop()
        spotifyAppRemote?.let {
            it.playerApi.pause()
            isPlaying = false
            val playButton = findViewById<ImageView>(R.id.Pause)
            playButton.setImageResource(R.drawable.play_arrow_black_24dp)
        }
    }
}