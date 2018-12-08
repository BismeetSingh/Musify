

package com.app.bissudroid.musify.events

import com.app.bissudroid.musify.models.Songs


class PlaybackState( val playing: Boolean){
    fun isSongPlaying(): Boolean {
        return this.playing

    }
}