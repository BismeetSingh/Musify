package com.app.bissudroid.musify.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreferenceUtils {



    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun getCurrentSong(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.CURRENT_SONG_NAME, "")
    }

    fun setCurrentSong(context: Context, newValue: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.CURRENT_SONG_NAME, newValue)
        editor.apply()
    }
    fun getCurrentSongPath(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.SONGPATH, "")
    }
    fun setCurrentAlbumid(context: Context, newValue: Long) {
        val editor = getSharedPreferences(context).edit()
        editor.putLong(Constants.ALBUM_ID, newValue)
        editor.apply()
    }
    fun getCurrentAlbumId(context: Context): Long? {
        return getSharedPreferences(context).getLong(Constants.ALBUM_ID, 0)
    }

    fun setCurrentArtist(context: Context, newValue: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.SONGARTIST, newValue)
        editor.apply()
    }
    fun getCurrentSongArtist(context: Context): String? {
        return getSharedPreferences(context).getString(Constants.SONGARTIST, "")
    }

    fun setCurrentSongPath(context: Context, newValue: String) {
        val editor = getSharedPreferences(context).edit()
        editor.putString(Constants.SONGPATH, newValue)
        editor.apply()
    }
    fun isPlaying(context: Context):Boolean{
        return getSharedPreferences(context).getBoolean(Constants.ISPLAYING,false)

    }
    fun savePlayingState(context: Context,isPlaying:Boolean){
        val editor = getSharedPreferences(context).edit()
        editor.putBoolean(Constants.ISPLAYING, isPlaying)
        editor.apply()
    }


}