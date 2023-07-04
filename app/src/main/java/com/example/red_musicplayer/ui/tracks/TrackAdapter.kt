package com.example.red_musicplayer.ui.tracks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.red_musicplayer.R
import com.example.red_musicplayer.databinding.SingleTrackCardBinding

class TrackAdapter(private val context: Context?, private var musicList: ArrayList<TracksViewModel>)
    : RecyclerView.Adapter<TrackAdapter.TrackHolder>() {


    class TrackHolder(binding : SingleTrackCardBinding): RecyclerView.ViewHolder(binding.root) {
        val songName = binding.songNameCard
        val artistName = binding.artistNameCard
        val songCover = binding.singleTrackListCover
        val songDuration = binding.durationTimeCard
        val root = binding.root


    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {

        holder.songName.text= musicList[position].songName
        holder.artistName.text = musicList[position].artistName
        holder.songDuration.text = formatDuration(musicList[position].duration)
        Glide.with(context!!).load(musicList[position].artURI)
            .apply(RequestOptions().placeholder(R.drawable.ic_default_music_red).centerCrop())
            .into(holder.songCover)

        holder.root.setOnClickListener {
            var bundle = Bundle()
            bundle.putInt("song-index",position)
            bundle.putString("class-name","TrackListAdapter")
            it.findNavController().navigate(R.id.action_all_tracks_to_musicPlayer,bundle)
        }
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        return TrackHolder(SingleTrackCardBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    fun updateTrackList(list : ArrayList<TracksViewModel>){
        musicList = ArrayList()
        musicList.addAll(list)
        notifyDataSetChanged()

    }


}