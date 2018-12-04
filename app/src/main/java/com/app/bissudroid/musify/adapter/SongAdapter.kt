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
      var mSelectedItemPosition = RecyclerView.NO_POSITION




    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): SongHolder {
       val musicItemsBinding= MusicItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SongHolder(musicItemsBinding,onSongClickListener)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun getItemCount(): Int {
        if(songItems!=null)
        return songItems!!.size
        else{
            return 0
//TODO fix crash here on data source
        }
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val uri = ContentUris.withAppendedId(
            Constants.URI,
            songItems!!.get(holder.adapterPosition).albumid)
        holder.bind(songItems!!.get(holder.adapterPosition),context,uri,position)
        if(mSelectedItemPosition!=position){
            holder.musicItemsBindingType.equalizerView.visibility = View.GONE
               holder.musicItemsBindingType.equalizerView.stopBars()
                songItems!![position].isPlaying = false
                SharedPreferenceUtils.savePlayingState(context, true)
        }






    }

    private fun toggleBars(
        position: Int,
        musicItemsBindingType: MusicItemsBinding
    ) {
//TODO fix animation.
            if (mSelectedItemPosition==position) {
                musicItemsBindingType.equalizerView.visibility = View.VISIBLE
                Timber.d("%s", musicItemsBindingType.equalizerView.toString())
                musicItemsBindingType.equalizerView.animateBars()
                if (!songItems!![position].isPlaying) {
                    Timber.d("Now pause Adapter")
                    SharedPreferenceUtils.savePlayingState(context, true)
                } else {
                    Timber.d("Now play Adapter")
                    SharedPreferenceUtils.savePlayingState(context, false)
                }
                songItems!![position].isPlaying = !songItems!![position].isPlaying
            }

//        notifyDataSetChanged()
//            } else {
//                Timber.d("Now play Adapter")
//                musicItemsBindingType.equalizerView.visibility = View.GONE
//               musicItemsBindingType.equalizerView.stopBars()
//                songItems!![position].isPlaying = false
//                SharedPreferenceUtils.savePlayingState(context, true)
//
//            }
        Timber.d("Bars playing and visible %s %s",musicItemsBindingType.equalizerView.isAnimating,
            musicItemsBindingType.equalizerView.visibility)


    }

    fun updateList( songItems:ArrayList<Songs>)
    {
        this.songItems=songItems
        if(SharedPreferenceUtils.getCurrentSong(context).isNullOrEmpty()){
            SharedPreferenceUtils.setCurrentSong(context,songItems.get(0).songName)
            SharedPreferenceUtils.setCurrentArtist(context,songItems.get(0).songArtist)
            SharedPreferenceUtils.setCurrentSongPath(context,songItems.get(0).songPath)
        }

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
          , position: Int

      ) {

          musicItemsBindingType.songs = musicitem
          musicItemsBindingType.root.setOnClickListener {

              mSelectedItemPosition = adapterPosition
              notifyDataSetChanged()
              Timber.d("%s",musicItemsBindingType.equalizerView.toString())

                toggleBars(position,musicItemsBindingType)



//              toggleBars(position, musicItemsBindingType)
              SharedPreferenceUtils.setCurrentSong(context, musicItemsBindingType.songName.text.toString())
              SharedPreferenceUtils.setCurrentSongPath(context, musicItemsBindingType.songs!!.songPath)
              SharedPreferenceUtils.setCurrentArtist(context, musicItemsBindingType.songArtist.text.toString())
              SharedPreferenceUtils.setCurrentAlbumid(context, musicitem.albumid)

              onSongClick.onSongClick(adapterPosition, musicitem)
//              notifyDataSetChanged()
//            }
          }
              Glide.with(context).load(uri).apply(RequestOptions().centerCrop().placeholder(R.drawable.musicicon))
                  .into(musicItemsBindingType.songThumbnail)
              musicItemsBindingType.executePendingBindings()
          }
      }


  }