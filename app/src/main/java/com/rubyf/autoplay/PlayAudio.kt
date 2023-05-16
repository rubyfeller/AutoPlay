package com.rubyf.autoplay

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

class PlayAudio() {
    private var mediaPlayer: MediaPlayer? = null

    fun playAudio(context: Context, resourceId: Int) {
        mediaPlayer = MediaPlayer.create(context, resourceId)
        mediaPlayer?.setOnPreparedListener {
            Log.d("Play audio", "Playing audio file")
            mediaPlayer!!.start()
        }
    }
}