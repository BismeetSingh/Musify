package com.app.bissudroid.musify.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.bissudroid.musify.R
import com.app.bissudroid.musify.adapter.SongAdapter
import com.app.bissudroid.musify.utils.Constants
import com.app.bissudroid.musify.utils.SongsManager
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.music_items.*
import timber.log.Timber

class Songs : Fragment() {
 lateinit   var songAdapter:SongAdapter


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
        songAdapter=SongAdapter(context!!)
//        musicList.addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
        musicList.adapter=songAdapter

        if(Build.VERSION.SDK_INT<23){
            songAdapter.updateList(SongsManager.getAllAudioFromDevice(activity!!))
        }
        else{
            getPermissions()
        }



    }

    private fun getPermissions() {

        val permissionCheck = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val readCheck=ContextCompat.checkSelfPermission(activity!!,Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED && readCheck!=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.MY_PERMISSIONS_REQUEST_READ_MEDIA
            )
        } else {
            songAdapter.updateList(SongsManager.getAllAudioFromDevice(context!!))
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.MY_PERMISSIONS_REQUEST_READ_MEDIA) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && permissions[1].equals(Manifest.permission.READ_EXTERNAL_STORAGE)
                && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                songAdapter.updateList(SongsManager.getAllAudioFromDevice(context!!))
                Timber.d("%d",songAdapter.itemCount)


            }
        }
    }


}