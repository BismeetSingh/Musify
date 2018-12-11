package com.app.bissudroid.musify.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.LocalBroadcastManager
import android.widget.RemoteViews
import com.app.bissudroid.musify.R
import com.app.bissudroid.musify.SongReceiver
import timber.log.Timber


class NotificationUtils(base: Context) : ContextWrapper(base) {


    private var mManager: NotificationManager? = null
    var songReceiver=SongReceiver()



    val manager: NotificationManager
        get() {
            if (mManager == null) {


                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val filter = IntentFilter()
                val pauseFilter=IntentFilter()
//
                filter.addAction(Constants.ACTION_PLAY)
                filter.addAction(Constants.ACTION_PAUSE)
//                pauseFilter.addAction(Constants.ACTION_PAUSE)
//
                registerReceiver(songReceiver, filter)
//                registerReceiver(songReceiver,pauseFilter)
//                Timber.d(LocalBroadcastManager.getInstance(applicationContext).toString())
//                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent(Constants.ACTION_PAUSE))
//                Timber.d(LocalBroadcastManager.getInstance(applicationContext).toString())


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
            androidChannel.enableVibration(false)

            // Sets the notification light color for notifications posted to this channel
            androidChannel.lightColor = Color.WHITE
            androidChannel.setSound(null, null)

            // Sets whether notifications posted to this channel appear on the lockscreen or not
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE


            manager.createNotificationChannel(androidChannel)


        }
    }

    //TODO show music control notifications
    fun getAndroidChannelNotification(title: String): NotificationCompat.Builder {
        val contentView: RemoteViews
        val playIntent:Intent
        if(!SharedPreferenceUtils.isPlaying(this))

        {
            playIntent = Intent(Constants.ACTION_PLAY)
             contentView = RemoteViews(packageName, R.layout.notification_layout)
//            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent(Constants.ACTION_PLAY))


//            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(this,SongReceiver::class.java))


        }
        else{
            playIntent = Intent(Constants.ACTION_PAUSE)

             contentView = RemoteViews(packageName, R.layout.notificationlayoutpause)
//            LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(Intent(Constants.ACTION_PAUSE))

           // sendBroadcast(Intent(this,SongReceiver::class.java))


        }

        contentView.setImageViewResource(R.id.image, R.drawable.musicicon)
        contentView.setTextViewText(R.id.songName, SharedPreferenceUtils.getCurrentSong(applicationContext))
        contentView.setTextViewText(R.id.songArtist, SharedPreferenceUtils.getCurrentSongArtist(applicationContext))
        val largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.musicicon)
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(SharedPreferenceUtils.getCurrentSongArtist(applicationContext))
        bigTextStyle.bigText(SharedPreferenceUtils.getCurrentSong(applicationContext))
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val pendingPlayIntent = PendingIntent.getBroadcast(this, 1, playIntent, 0)



        contentView.setOnClickPendingIntent(R.id.pauseNotificationSong, pendingPlayIntent)

        return NotificationCompat.Builder(applicationContext, ANDROID_CHANNEL_ID)
//            .setStyle(bigTextStyle)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.musicicon)
            .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
//            .setLargeIcon(largeIconBitmap)
//            .addAction(playAction)
//            .addAction(prevAction)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setVibrate(longArrayOf(-1))
            .setSound(null)
//            .setFullScreenIntent(pendingIntent,true)
            .setSmallIcon(android.R.drawable.stat_notify_more)
            .setAutoCancel(true)
            .setContent(contentView)

    }

    companion object {
        val ANDROID_CHANNEL_ID = "com.bismeet.musify"

        val ANDROID_CHANNEL_NAME = "ANDROID CHANNEL"
    }

}

