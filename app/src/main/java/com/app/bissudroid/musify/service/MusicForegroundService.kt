package com.app.bissudroid.musify.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.app.bissudroid.musify.R
import com.app.bissudroid.musify.utils.NotificationUtils
import timber.log.Timber

class MusicForegroundService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
    val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
    val ACTION_PAUSE = "ACTION_PAUSE"
    val ACTION_PLAY = "ACTION_PLAY"

   override fun onCreate() {
        super.onCreate()
        Timber.d("My foreground service onCreate().")
    }
   override fun onStartCommand(intent:Intent, flags:Int, startId:Int):Int {

            val action = intent.getAction()
            when (action) {
                ACTION_START_FOREGROUND_SERVICE -> {
                    startForegroundService()
                    Toast.makeText(getApplicationContext(), "Foreground service is started.", Toast.LENGTH_LONG).show()
                }
                ACTION_STOP_FOREGROUND_SERVICE -> {
                    stopForegroundService()
                    Toast.makeText(getApplicationContext(), "Foreground service is stopped.", Toast.LENGTH_LONG).show()
                }
                ACTION_PLAY -> Toast.makeText(getApplicationContext(), "You click Play button.", Toast.LENGTH_LONG).show()
                ACTION_PAUSE -> Toast.makeText(getApplicationContext(), "You click Pause button.", Toast.LENGTH_LONG).show()

        }
        return super.onStartCommand(intent, flags, startId)
    }
    /* Used to build and start foreground service. */
    private fun startForegroundService() {
        val notificationUtils = NotificationUtils(baseContext)
        val builder = notificationUtils.getAndroidChannelNotification("Failed", "Failed  to add batches ")
        val notification = builder.build()
        // Start foreground service.
        startForeground(1, notification)
    }
    private fun stopForegroundService() {

        // Stop foreground service and remove the notification.
        stopForeground(true)
        // Stop the foreground service.
        stopSelf()
    }
}