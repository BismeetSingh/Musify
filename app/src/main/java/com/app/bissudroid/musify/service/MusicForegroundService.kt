package com.app.bissudroid.musify.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import com.app.bissudroid.musify.utils.Constants
import com.app.bissudroid.musify.utils.NotificationUtils
import com.app.bissudroid.musify.utils.SharedPreferenceUtils
import timber.log.Timber

class MusicForegroundService: Service(),MediaPlayer.OnPreparedListener{
    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
    }

    lateinit var mediaPlayer: MediaPlayer

    private var songName:String=""
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }




   override fun onCreate() {
        super.onCreate()
       mediaPlayer=MediaPlayer()


    }
   override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int {


            val action = intent.getAction()
                songName=intent.getStringExtra(Constants.SONGPATH)
           when (action) {
                Constants.ACTION_START_FOREGROUND_SERVICE -> {
                    startForegroundService()
                    Toast.makeText(applicationContext, "Foreground service is started.", Toast.LENGTH_LONG).show()
                }
                Constants.ACTION_STOP_FOREGROUND_SERVICE -> {
                    stopForegroundService()
                    Toast.makeText(applicationContext, "Foreground service is stopped.", Toast.LENGTH_LONG).show()
                }
                Constants.ACTION_PLAY -> {
                    playSong()
//                    Toast.makeText(getApplicationContext(), "You click Play button.", Toast.LENGTH_LONG).show()
                }
                Constants.ACTION_PAUSE -> {
                    pauseSong()

                }
               Constants.ACTION_RESUME ->{
                   resumeSong()

               }

        }
        return super.onStartCommand(intent, flags, startId)
    }
    /* Used to build and start foreground service. */
    private fun startForegroundService() {
        val notificationUtils = NotificationUtils(baseContext)
        val builder = notificationUtils.getAndroidChannelNotification("Now Playing", songName)
        val notification = builder.build()

        startForeground(1, notification)
    }

    private fun playSong() {
        Timber.d(songName)
//TODO fix crash here on data source
        if (!mediaPlayer.isPlaying) {

            mediaPlayer.setDataSource(songName)
            mediaPlayer.setOnPreparedListener(this)
            mediaPlayer.prepareAsync()

        } else if (SharedPreferenceUtils.isPlaying(applicationContext) && mediaPlayer.isPlaying) {


            mediaPlayer.stop()
            mediaPlayer.reset()


            mediaPlayer.setDataSource(songName)

            mediaPlayer.setOnPreparedListener(this)
            mediaPlayer.prepareAsync()


        }
    }
    private fun pauseSong(){
        mediaPlayer.pause()
    }
    private fun resumeSong(){
        if(!songName.isEmpty())
        mediaPlayer.start()

    }

    private fun stopForegroundService() {

        // Stop foreground service and remove the notification.
        stopForeground(true)
        // Stop the foreground service.
        stopSelf()
    }
}