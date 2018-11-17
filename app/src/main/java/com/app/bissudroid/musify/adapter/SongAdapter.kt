package com.app.bissudroid.musify.adapter

import android.content.ContentUris
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.app.bissudroid.musify.databinding.MusicItemsBinding
import com.app.bissudroid.musify.interfaces.onSongClickListener
import com.app.bissudroid.musify.models.Songs
import com.app.bissudroid.musify.utils.Constants


class SongAdapter(val context: Context,val onSongClickListener: onSongClickListener) : RecyclerView.Adapter<SongHolder>() {
    private var songItems: ArrayList<Songs>?=null



    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SongHolder {
       val musicItemsBinding= MusicItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SongHolder(musicItemsBinding,onSongClickListener)
    }

    override fun getItemCount(): Int {
        if(songItems!=null)
        return songItems!!.size
        else{
            return 0
        }
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val uri = ContentUris.withAppendedId(
            Constants.URI,
            songItems!!.get(position).albumid)
        holder.bind(songItems!!.get(position),context,uri)


    }

    fun updateList( songItems:ArrayList<Songs>)
    {
        this.songItems=songItems
        notifyDataSetChanged()

    }
}