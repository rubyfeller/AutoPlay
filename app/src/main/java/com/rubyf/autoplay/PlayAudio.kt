package com.rubyf.autoplay

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class PlayAudio() {
    private var mediaPlayer: MediaPlayer? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun playAudio(context: Context, resourceId: Int) {
        mediaPlayer = MediaPlayer.create(context, resourceId)
        mediaPlayer?.setOnPreparedListener {
            Log.d("Play audio", "Playing audio file")
            SpotifyControllerService.pause()
            mediaPlayer!!.start()
            mediaPlayer?.setOnCompletionListener {
                mediaPlayer!!.release()
                SpotifyControllerService.resume()
            }
        }
    }
    fun playNews() {
            coroutineScope.launch {
                try {
                    val mp3Url = withContext(Dispatchers.IO) { getMp3UrlFromXml() }
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(mp3Url)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    SpotifyControllerService.pause()

                    // Release the MediaPlayer object when the audio playback is finished and restart Spotify
                    mediaPlayer.setOnCompletionListener {
                        mediaPlayer.release()
                        SpotifyControllerService.resume()
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
        // Loop through the entries to find the MP3 URL
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
}