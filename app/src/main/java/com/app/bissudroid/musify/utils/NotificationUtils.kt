package com.app.bissudroid.musify.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.widget.RemoteViews
import com.app.bissudroid.musify.R
import com.app.bissudroid.musify.SongReceiver
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.media.session.MediaButtonReceiver
import com.app.bissudroid.musify.R.id.currentSong
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.bottom_now_playing_view.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.io.FileNotFoundException


class NotificationUtils(base: Context) : ContextWrapper(base) {


    private var mManager: NotificationManager? = null
    var songReceiver=SongReceiver()



    val manager: NotificationManager
        get() {
            if (mManager == null) {


                mManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val filter = IntentFilter()
                filter.addAction(Constants.ACTION_PLAY)
                filter.addAction(Constants.ACTION_PAUSE)

                registerReceiver(songReceiver, filter)



            }

            return mManager!!
        }


    init {
        createChannels()
    }
    public fun unregisterReceiver(){
        unregisterReceiver(songReceiver)
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
            androidChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC


            manager.createNotificationChannel(androidChannel)


        }
    }

    //TODO show music control notifications
    fun getAndroidChannelNotification(title: String): NotificationCompat.Builder {

        val playIntent:Intent
        val  action:NotificationCompat.Action


        if(!SharedPreferenceUtils.isPlaying(this))

        {
            playIntent = Intent(Constants.ACTION_PLAY)
            action = NotificationCompat.Action(R.drawable.play_music,"Play",null)
//             contentView = RemoteViews(packageName, R.layout.notification_layout)

        }
        else{
            playIntent = Intent(Constants.ACTION_PAUSE)
            action = NotificationCompat.Action(R.drawable.pause_music,"Pause",null)

        }
        val pendingPlayIntent = PendingIntent.getBroadcast(this, 1, playIntent, 0)
        action.actionIntent=pendingPlayIntent;


        val largeIconBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.musicicon)
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(SharedPreferenceUtils.getCurrentSongArtist(applicationContext))
        bigTextStyle.bigText(SharedPreferenceUtils.getCurrentSong(applicationContext))
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)



        action.actionIntent=pendingPlayIntent;
        var iconBitmap:Bitmap
        val uri = ContentUris.withAppendedId(
            Constants.URI,
            SharedPreferenceUtils.getCurrentAlbumId(applicationContext!!)?.toLong()!!)
            try {
                iconBitmap=MediaStore.Images.Media.getBitmap(contentResolver,uri)
            }
            catch ( filenotfound:FileNotFoundException){
                iconBitmap= getBitmapFromVectorDrawable(R.drawable.musicicon)!!
            }






//        contentView.setOnClickPendingIntent(R.id.pauseNotificationSong, pendingPlayIntent)

        return NotificationCompat.Builder(applicationContext, ANDROID_CHANNEL_ID)
            .setStyle(MediaStyle().setShowCancelButton(true).setCancelButtonIntent(MediaButtonReceiver.buildMediaButtonPendingIntent(applicationContext,1)
            ).setShowActionsInCompactView(0,1,2))
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.notification_icon)

            .setBadgeIconType(R.drawable.musicicon)
            .setContentTitle(SharedPreferenceUtils.getCurrentSong(applicationContext))
            .setContentText(SharedPreferenceUtils.getCurrentSongArtist(applicationContext))
            .setLargeIcon(iconBitmap)
            .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)

            .addAction(R.drawable.previoussong,"Previous", null)
            .addAction(action)
            .addAction(R.drawable.nextsong,"Next",null)
//            .setLargeIcon(largeIconBitmap)

            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setVibrate(longArrayOf(-1))
            .setSound(null)
//            .setFullScreenIntent(pendingIntent,true)


            .setAutoCancel(true)
            .setShowWhen(true)


    }

    companion object {
        val ANDROID_CHANNEL_ID = "com.bismeet.musify"

        val ANDROID_CHANNEL_NAME = "ANDROID CHANNEL"
    }
    fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(this, drawableId) ?: return null

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable).mutate()
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

}

