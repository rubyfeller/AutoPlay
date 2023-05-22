package com.rubyf.autoplay

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track

object SpotifyControllerService {
    private const val clientId = "02c259bc614941c4beea5e1e06d826ff"
    private const val redirectUri = "http://localhost"

    private var spotifyAppRemote: SpotifyAppRemote? = null
    private var connectionParams = ConnectionParams.Builder(clientId).setRedirectUri(redirectUri).showAuthView(true).build()

    fun connect(context: Context, handler: (connected: Boolean) -> Unit) {
        if (spotifyAppRemote?.isConnected == true) {
            handler(true)
            return
        }
        val connectionListener = object : Connector.ConnectionListener {
            override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                this@SpotifyControllerService.spotifyAppRemote = spotifyAppRemote
                Log.d("SpotifyService", "Connected")
                handler(true)
            }
            override fun onFailure(throwable: Throwable) {
                Log.e("SpotifyService", throwable.message, throwable)
                handler(false)
            }
        }
        SpotifyAppRemote.connect(context, connectionParams, connectionListener)
    }

    fun playPlaylist(uri: String) {
        Log.d("SpotifyService", "Playing playlist $uri")
        spotifyAppRemote?.playerApi?.play(uri)
    }

    fun getTrack(handler: (Track) -> Unit) {
        spotifyAppRemote?.playerApi?.subscribeToPlayerState()?.setEventCallback {
            handler(it.track)
        }
    }

    fun skipPrevious() {
        spotifyAppRemote?.playerApi?.skipPrevious()
    }

    fun pause() {
        spotifyAppRemote?.playerApi?.pause()
    }

    fun resume() {
        spotifyAppRemote?.playerApi?.resume()
    }

    fun skipNext() {
        spotifyAppRemote?.playerApi?.skipNext()
        }
    }