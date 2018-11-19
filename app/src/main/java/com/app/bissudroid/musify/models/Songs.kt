package com.app.bissudroid.musify.models


  data class Songs(
      var songName: String,
      var songArtist: String,
      var isPlaying: Boolean,
      var songDuration: String,
      var songAlbum: String,
      var songPath: String,
      var albumid: Long

  ) {

}