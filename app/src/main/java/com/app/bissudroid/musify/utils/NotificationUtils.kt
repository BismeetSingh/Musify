package com.app.bissudroid.musify.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.media.session.PlaybackState.ACTION_PAUSE
import android.media.session.PlaybackState.ACTION_PLAY
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import com.app.bissudroid.musify.R
import com.app.bissudroid.musify.service.MusicForegroundService


class NotificationUtils(base: Context) : ContextWrapper(base) {
    val ACTION_PAUSE = "ACTION_PAUSE"
    val ACTION_PLAY = "ACTION_PLAY"

    private var mManager: NotificationManager? = null

    val manager: NotificationManager
        get() {
            if (mManager == null) {

                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            }
            return mManager!!
        }


    init {
        createChannels()
    }

    fun createChannels() {

        // create android channel
        val androidChannel: NotificationChannel?
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            androidChannel = NotificationChannel(
                ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )


            // Sets whether notifications posted to this channel should display notification lights
            androidChannel.enableLights(true)
            // Sets whether notification posted to this channel should vibrate.
            androidChannel.enableVibration(true)
            // Sets the notification light color for notifications posted to this channel
            androidChannel.lightColor = Color.GREEN

            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE


            manager.createNotificationChannel(androidChannel)


        }
    }

    fun getAndroidChannelNotification(title: String, body: String): NotificationCompat.Builder {
        val largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.musicicon)
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle("Music player implemented by foreground service.")
        val intent = Intent()
        bigTextStyle.bigText("Android foreground service is a android service which can run in foreground always, it can be controlled by user via notification.")
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val playIntent = Intent(this, MusicForegroundService::class.java)
        playIntent.setAction(ACTION_PLAY)
        val pendingPlayIntent = PendingIntent.getService(this, 0, playIntent, 0)
        val playAction = NotificationCompat.Action(R.drawable.play_music, "Play", pendingPlayIntent)

        val pauseIntent = Intent(this, MusicForegroundService::class.java)
        pauseIntent.setAction(ACTION_PAUSE)
        val pendingPrevIntent = PendingIntent.getService(this, 0, pauseIntent, 0)
        val prevAction = NotificationCompat.Action(android.R.drawable.ic_media_pause, "Pause", pendingPrevIntent)
        return NotificationCompat.Builder(applicationContext, ANDROID_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(bigTextStyle)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.musicicon)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setLargeIcon(largeIconBitmap)
            .addAction(playAction)
            .addAction(prevAction)
            .setFullScreenIntent(pendingIntent,true)
            .setSmallIcon(android.R.drawable.stat_notify_more)
            .setAutoCancel(true)
    }

    companion object {
        val ANDROID_CHANNEL_ID = "com.bismeet.musify"

        val ANDROID_CHANNEL_NAME = "ANDROID CHANNEL"
    }

}

