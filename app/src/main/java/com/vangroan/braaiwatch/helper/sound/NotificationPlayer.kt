package com.vangroan.braaiwatch.helper.sound

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import java.io.IOException

/**
 * Helper class to play the default phone notification sound.
 *
 * https://developer.android.com/guide/topics/media/mediaplayer.html
 * http://stackoverflow.com/questions/10335057/play-notification-default-sound-only-android
 *
 * Created by Victor on 2016/12/23.
 */
class NotificationPlayer(context: Context) {

    private var player: MediaPlayer? = null
    private val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    init {
        try {
            // Constructing media player in init block to catch exceptions
            player = MediaPlayer()
            player!!.setAudioStreamType(AudioManager.STREAM_NOTIFICATION)
            player!!.setDataSource(context, uri)
            player!!.prepare() // Buffering
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun play() {
        player?.start()
    }

    fun destroy() {
        player?.release()
        player = null
    }
}