package com.app.bissudroid.musify.adapter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import com.app.bissudroid.musify.R
import com.app.bissudroid.musify.databinding.MusicItemsBinding
import com.app.bissudroid.musify.models.Songs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class SongHolder(musicItemsBinding: MusicItemsBinding) : RecyclerView.ViewHolder(musicItemsBinding.root) {
    var musicItemsBindingType=musicItemsBinding;

    fun bind(musicitem: Songs,context:Context,uri:Uri) {
        musicItemsBindingType.songs=musicitem
        Glide.with(context).load(uri).apply(RequestOptions().centerCrop().placeholder(R.drawable.musicicon)).into(musicItemsBindingType.songThumbnail)
        musicItemsBindingType.executePendingBindings()
    }


}
