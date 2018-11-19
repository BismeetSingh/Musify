package com.app.bissudroid.musify.adapter

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.bissudroid.musify.R
import com.app.bissudroid.musify.databinding.MusicItemsBinding
import com.app.bissudroid.musify.interfaces.onSongClickListener
import com.app.bissudroid.musify.models.Songs
import com.app.bissudroid.musify.utils.Constants
import com.app.bissudroid.musify.utils.SharedPreferenceUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import timber.log.Timber


class SongAdapter(val context: Context,val onSongClickListener: onSongClickListener) : RecyclerView.Adapter<SongAdapter.SongHolder>() {
    private var songItems: ArrayList<Songs>?=null
      var mSelectedItemPosition = -1


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
            songItems!!.get(holder.adapterPosition).albumid)
        toggleBars(position, holder)

        holder.bind(songItems!![position],context,uri)





    }

    private fun toggleBars(position: Int, holder: SongHolder) {

        Timber.d("%s",songItems!![position].isPlaying)
        if (mSelectedItemPosition == position) {
            holder.musicItemsBindingType.equalizerView.visibility = View.VISIBLE
            holder.musicItemsBindingType.equalizerView.animateBars()


            if (!songItems!![position].isPlaying) {

                SharedPreferenceUtils.savePlayingState(context,true)

            } else {

                SharedPreferenceUtils.savePlayingState(context,false)

            }
            songItems!![position].isPlaying = !songItems!![position].isPlaying
        } else {
            holder.musicItemsBindingType.equalizerView.visibility = View.GONE
            holder.musicItemsBindingType.equalizerView.stopBars()
            songItems!![position].isPlaying = false
            SharedPreferenceUtils.savePlayingState(context,true)

        }
    }

    fun updateList( songItems:ArrayList<Songs>)
    {
        this.songItems=songItems
        notifyDataSetChanged()

    }

  inner  class SongHolder(
        musicItemsBinding: MusicItemsBinding,
        onSongClickListener: onSongClickListener
    ) : RecyclerView.ViewHolder(musicItemsBinding.root) {
        var musicItemsBindingType = musicItemsBinding



        var onSongClick = onSongClickListener

        fun bind(
            musicitem: Songs,
            context: Context,
            uri: Uri


        ) {

            musicItemsBindingType.songs = musicitem
            musicItemsBindingType.root.setOnClickListener {
                mSelectedItemPosition=adapterPosition
                SharedPreferenceUtils.setCurrentSong(context, musicItemsBindingType.songName.text.toString())

                onSongClick.onSongClick(adapterPosition, musicitem)
                notifyDataSetChanged()
            }
            Glide.with(context).load(uri).apply(RequestOptions().centerCrop().placeholder(R.drawable.musicicon))
                .into(musicItemsBindingType.songThumbnail)
            musicItemsBindingType.executePendingBindings()
        }
    }




}