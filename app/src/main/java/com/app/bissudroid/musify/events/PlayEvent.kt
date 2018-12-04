package com.app.bissudroid.musify.events



class PlayEvent(private val startSongId: Long) {

    fun getStartSongId(): Long {
        return this.startSongId

    }
}