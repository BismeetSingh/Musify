

package com.app.bissudroid.musify.events

import com.app.bissudroid.musify.models.Songs


class PlaybackState(val song: Songs?, val playing: Boolean, val songDuration: Int, val currentDuration: Int)