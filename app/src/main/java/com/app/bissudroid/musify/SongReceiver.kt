package com.app.bissudroid.musify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.app.bissudroid.musify.adapter.SongAdapter
import com.app.bissudroid.musify.events.AdapterState
import com.app.bissudroid.musify.events.FragmentNotifierPlayBack
import com.app.bissudroid.musify.events.PlaybackState
import com.app.bissudroid.musify.events.RxBus
import com.app.bissudroid.musify.utils.Constants
import com.app.bissudroid.musify.utils.SharedPreferenceUtils
import timber.log.Timber

class SongReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        Timber.d(intent?.action)
        Toast.makeText(context,intent?.action,Toast.LENGTH_SHORT).show()
        if(context!=null) {




//            startForeground(1, notification)
            when (intent?.action) {
                Constants.ACTION_PLAY -> {

                    SharedPreferenceUtils.savePlayingState(context, true)
                    RxBus.publish(PlaybackState(true))
                    RxBus.publish(FragmentNotifierPlayBack(true))
                Toast.makeText(context,"Playing",Toast.LENGTH_SHORT).show()

                }
                Constants.ACTION_PAUSE -> {

                    SharedPreferenceUtils.savePlayingState(context, false)
                    RxBus.publish(PlaybackState(false))
                    RxBus.publish(FragmentNotifierPlayBack(false))
                    Toast.makeText(context,"Pause",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}