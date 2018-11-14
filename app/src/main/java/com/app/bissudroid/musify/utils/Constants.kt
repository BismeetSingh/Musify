package com.app.bissudroid.musify.utils

import android.net.Uri

class Constants {
    companion object {
        val MY_PERMISSIONS_REQUEST_READ_MEDIA:Int=10
        val URI:Uri=Uri.parse("content://media/external/audio/albumart")
    }
}