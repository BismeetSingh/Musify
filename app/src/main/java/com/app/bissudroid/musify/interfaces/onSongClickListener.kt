package com.app.bissudroid.musify.interfaces

import com.app.bissudroid.musify.models.Songs

interface onSongClickListener {
    fun onSongClick( pos:Int,songItem:Songs)
}