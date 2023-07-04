package com.example.red_musicplayer.ui.tracks

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.red_musicplayer.MainActivity
import com.example.red_musicplayer.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TracksFragments : Fragment() {

    private var parentActivity: MainActivity?=null
    var mTrackList: ArrayList<TracksViewModel>? = null


    private lateinit var arrSortingCriteria : Array<String>
    private var filter : String = MediaStore.Audio.Media.TITLE
     lateinit var rv : RecyclerView

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.fragment_track,container,false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textHome = view.findViewById<TextView>(R.id.text_home)
        textHome.text ="Tracks"
        textHome.setOnClickListener {
            it.findNavController().navigate(R.id.action_all_tracks_to_musicPlayer)
        }

        if (activity is MainActivity) parentActivity = activity as MainActivity

        rv = view.findViewById(R.id.rv_tracksFragment)
        parentActivity?.mMusicInfo = parentActivity!!.fetchAudio()

        rv.layoutManager = LinearLayoutManager(activity)
        rv.setHasFixedSize(true)
        parentActivity?.trackAdapter= TrackAdapter(requireContext(),  parentActivity?.mMusicInfo!!)
        rv.adapter = parentActivity!!.trackAdapter

        val refreshContainer : SwipeRefreshLayout = view.findViewById(R.id.refreshLayoutContainer)
        refreshContainer.setOnRefreshListener{

            TrackAdapter(requireContext(),parentActivity!!.mMusicInfo).updateTrackList( parentActivity?.mMusicInfo!!)
            refreshContainer.isRefreshing = false

        }

        val sortingBtn = view.findViewById<ImageView>(R.id.menuBtnTrackList)
        sortingBtn.setOnClickListener { parentActivity!!.sortingTracks() }
    }




}