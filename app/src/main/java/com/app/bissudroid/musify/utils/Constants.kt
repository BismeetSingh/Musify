package com.app.bissudroid.musify.utils

import android.net.Uri

class Constants {
    companion object {
        val MY_PERMISSIONS_REQUEST_READ_MEDIA:Int=10
        val URI:Uri=Uri.parse("content://media/external/audio/albumart")
        val SHARED_PREFERENCE_NAME:String="MusicPreferences"
        val CURRENT_SONG_NAME:String="currentSongName"
        val ACTION_START_FOREGROUND_SERVICE:String="ACTION_START_FOREGROUND_SERVICE"
        val SONGPATH:String="songpath"
        val ISPLAYING:String="isplaying"
        val ACTION_PAUSE = "ACTION_PAUSE"
        val ACTION_PLAY = "ACTION_PLAY"
        val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"


    }
}