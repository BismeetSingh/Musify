package com.app.bissudroid.musify.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.app.bissudroid.musify.R
import com.app.bissudroid.musify.databinding.MusicItemsBinding
import com.app.bissudroid.musify.interfaces.onSongClickListener
import com.app.bissudroid.musify.models.Songs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import timber.log.Timber

class SongHolder(
    musicItemsBinding: MusicItemsBinding,
    onSongClickListener: onSongClickListener
) : RecyclerView.ViewHolder(musicItemsBinding.root) {
    var musicItemsBindingType=musicItemsBinding
    var onSongClick=onSongClickListener

    fun bind(musicitem: Songs,context:Context,uri:Uri) {
        musicItemsBindingType.songs=musicitem
        musicItemsBindingType.root.setOnClickListener {
            musicItemsBindingType.equalizerView.visibility=View.VISIBLE
            musicItemsBindingType.equalizerView.animateBars()
            onSongClick.onSongClick(adapterPosition,musicitem)
        }
        Glide.with(context).load(uri).apply(RequestOptions().centerCrop().placeholder(R.drawable.musicicon)).into(musicItemsBindingType.songThumbnail)
        musicItemsBindingType.executePendingBindings()
         }
    }



