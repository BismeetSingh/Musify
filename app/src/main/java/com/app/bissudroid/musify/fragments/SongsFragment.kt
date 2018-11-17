package com.app.bissudroid.musify.fragments

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.bissudroid.musify.R
import com.app.bissudroid.musify.adapter.SongAdapter
import com.app.bissudroid.musify.interfaces.onSongClickListener
import com.app.bissudroid.musify.models.Songs
import com.app.bissudroid.musify.utils.Constants
import com.app.bissudroid.musify.utils.SongsManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.bottom_now_playing_view.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.music_items.*
import timber.log.Timber
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.content.Intent
import com.app.bissudroid.musify.service.MusicForegroundService


class SongsFragment : Fragment(),onSongClickListener,MediaPlayer.OnPreparedListener{
    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()

    }
//TODO move this to service
    override fun onSongClick(pos: Int, songItem: Songs) {
    if (!mediaPlayer.isPlaying){
        songName=songItem.songPath
        mediaPlayer.setDataSource(songItem.songPath)
        mediaPlayer.setOnPreparedListener(this)
        mediaPlayer.prepareAsync()
        val intent = Intent(activity, MusicForegroundService::class.java)
        intent.setAction("ACTION_START_FOREGROUND_SERVICE")
        activity!!.startService(intent)
    }
       else if (!songName.equals(songItem.songPath,true) && mediaPlayer.isPlaying) {


            mediaPlayer.stop()
            mediaPlayer.reset()


            songName = songItem.songPath
            mediaPlayer.setDataSource(songItem.songPath)

            mediaPlayer.setOnPreparedListener(this)
            mediaPlayer.prepareAsync()


        }
    currentSong.songName.text = songItem.songName
    currentSong.songArtist.text = songItem.songArtist
    val uri = ContentUris.withAppendedId(
        Constants.URI,
        songItem.albumid
    )
    Glide.with(context!!).load(uri).apply(RequestOptions().centerCrop().placeholder(R.drawable.musicicon))
        .into(currentSong.songThumbnail)


    }

    private lateinit   var songAdapter:SongAdapter
    private lateinit var mediaPlayer: MediaPlayer
    private var songName:String=""




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        musicList.setHasFixedSize(false)
        musicList.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        val view=LayoutInflater.from(context).inflate(R.layout.empty_view,root,false);
        musicList.setEmptyView(view)
        songAdapter=SongAdapter(context!!,this)
        mediaPlayer=MediaPlayer()
//        musicList.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))



        if(Build.VERSION.SDK_INT<23){
            songAdapter.updateList(SongsManager.getAllAudioFromDevice(context!!))
        }
        else{
            getPermissions()
        }
        musicList.adapter=songAdapter




    }

    private fun getPermissions() {

        val permissionCheck = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readCheck=ContextCompat.checkSelfPermission(activity!!,Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED && readCheck!=PackageManager.PERMISSION_GRANTED) {
            requestPermissions(

                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.MY_PERMISSIONS_REQUEST_READ_MEDIA
            )
        } else {
            songAdapter.updateList(SongsManager.getAllAudioFromDevice(context!!))
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Timber.d("called");

        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_READ_MEDIA) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                songAdapter.updateList(SongsManager.getAllAudioFromDevice(context!!))


            }
        }
    }



}