package com.example.red_musicplayer.ui.albums

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.red_musicplayer.R
import com.example.red_musicplayer.databinding.AlbumCardBinding
import com.example.red_musicplayer.ui.tracks.TracksViewModel

class AlbumAdapter(private val context: Context,private var albumList: ArrayList<TracksViewModel>)
    : RecyclerView.Adapter<AlbumAdapter.AlbumHolder>() {

    class AlbumHolder(binding: AlbumCardBinding) : RecyclerView.ViewHolder(binding.root) {

        val albumCover = binding.singCoverAlbum
        val albumName = binding.albumName
        val albumYear = binding.albumYear
        val root = binding.root

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumHolder {
        return AlbumHolder(AlbumCardBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: AlbumHolder, position: Int) {
        Glide.with(context!!).load(albumList[position].artURI)
            .apply(RequestOptions().placeholder(R.drawable.ic_default_music_red).centerCrop())
            .into(holder.albumCover)
        holder.albumName.text = albumList[position].album
        holder.albumYear.text = null
    }

    override fun getItemCount(): Int {
        return albumList.size
    }

    fun updateTrackList(list : ArrayList<TracksViewModel>){

        albumList = ArrayList()
        albumList.addAll(list)
        notifyDataSetChanged()

    }
}