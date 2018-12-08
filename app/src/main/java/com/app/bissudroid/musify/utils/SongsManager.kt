package com.app.bissudroid.musify.utils

import android.content.Context
import android.provider.MediaStore
import com.app.bissudroid.musify.models.Songs
import timber.log.Timber

import java.util.ArrayList

object SongsManager {
    fun getAllAudioFromDevice(context: Context): ArrayList<Songs> {

        val tempAudioList = ArrayList<Songs>()

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.ALBUM,
            MediaStore.Audio.ArtistColumns.ARTIST
        ,MediaStore.Audio.Albums.ALBUM_ID,
            MediaStore.Audio.Media._ID
        )
        val c = context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            MediaStore.Audio.Media.TITLE + " COLLATE NOCASE ASC"
        )

        if (c != null) {

            Timber.d("%d",c.count)
            while (c.moveToNext()) {
                val path = c.getString(0)
                val name = path.substring(path.lastIndexOf("/") + 1)
                val  duration= convertDuration(c.getString(1).toLong())
                val album = c.getString(2)
                val artist = c.getString(3)
                val albumid=c.getInt(4)
                val idColumn = c.getLong(5);
                val audioModel = Songs(name.  substring(0,name.lastIndexOf(".")), artist, false, duration, album, path,albumid,idColumn)
                tempAudioList.add(audioModel)
            }
            c.close()
        }


        return tempAudioList
    }

    fun convertDuration(duration: Long): String {
        var out: String? = null
        val hours: Long
        try {
            hours = duration / 3600000
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return out!!
        }

        val remaining_minutes = (duration - hours * 3600000) / 60000
        var minutes = remaining_minutes.toString()
        if (minutes.equals("0")) {
            minutes = "00"
        }
        val remaining_seconds = duration - hours * 3600000 - remaining_minutes * 60000
        var seconds = remaining_seconds.toString()
        if (seconds.length < 2) {
            seconds = "00"
        } else {
            seconds = seconds.substring(0, 2)
        }

        if (hours > 0) {
            out = hours.toString() + ":" + minutes + ":" + seconds
        } else {
            out = "$minutes:$seconds"
        }

        return out

    }
}
