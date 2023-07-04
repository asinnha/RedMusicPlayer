package com.example.red_musicplayer.ui.albums

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.red_musicplayer.MainActivity
import com.example.red_musicplayer.R

class AlbumFragment : Fragment() {

    private var parentActivity: MainActivity?=null

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_album ,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity is MainActivity) parentActivity = activity as MainActivity

        val albumTitle = view.findViewById<TextView>(R.id.textAlbum)
        albumTitle.text = "Albums"

        val rvAlbum = view.findViewById<RecyclerView>(R.id.rvAlbum)
        parentActivity?.mMusicInfo = parentActivity?.fetchAudio()!!
        rvAlbum.layoutManager = GridLayoutManager(requireContext(),2)
        rvAlbum.setHasFixedSize(true)
        rvAlbum.adapter = AlbumAdapter(requireContext(), parentActivity?.mMusicInfo!!)

        val refreshContainer : SwipeRefreshLayout = view.findViewById(R.id.albumRefreshContainer)
        refreshContainer.setOnRefreshListener{
            parentActivity?.mMusicInfo = parentActivity?.fetchAudio()!!
            AlbumAdapter(requireContext(), parentActivity?.mMusicInfo!!).updateTrackList(parentActivity?.mMusicInfo!!)
            refreshContainer.isRefreshing = false

        }
    }


}