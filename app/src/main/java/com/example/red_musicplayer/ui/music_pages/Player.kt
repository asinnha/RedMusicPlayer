package com.example.red_musicplayer.ui.music_pages

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.red_musicplayer.MainActivity
import com.example.red_musicplayer.MusicService
import com.example.red_musicplayer.R
import com.example.red_musicplayer.ui.tracks.TracksFragments
import com.example.red_musicplayer.ui.tracks.TracksViewModel
import com.example.red_musicplayer.ui.tracks.formatDuration
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView


class Player : Fragment() {

    private lateinit var bottomNavbar: BottomNavigationView
    private var position: Int =0
    private var playerMusicList: ArrayList<TracksViewModel> = ArrayList()
    private lateinit var songCover:ShapeableImageView
    private lateinit var artistName : TextView
    private lateinit var songName : TextView
    private lateinit var currentTimeStamp : TextView
    private lateinit var totalTimeStamp : TextView
    private lateinit var play : ImageView
    private lateinit var next : ImageView
    private lateinit var previous : ImageView
    private lateinit var seekBar : SeekBar
    private var isPlaying: Boolean = false
    private lateinit var runnable: Runnable

    private var parentActivity: MainActivity?=null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songName = view.findViewById(R.id.songName)
        artistName = view.findViewById(R.id.artistName)
        songCover = view.findViewById(R.id.musicCover)
        play = view.findViewById(R.id.playBtn)
        next = view.findViewById(R.id.nextBtn)
        previous = view.findViewById(R.id.previousBtn)
        currentTimeStamp = view.findViewById(R.id.currentTimeStamp)
        totalTimeStamp = view.findViewById(R.id.totalTimeStamp)
        seekBar = view.findViewById(R.id.seekerBar)

        bottomNavbar = requireActivity().findViewById(R.id.nav_view)
        bottomNavbar.visibility = View.GONE
        if (activity is MainActivity) parentActivity = activity as MainActivity
        val bundle = arguments
        val trackListFrag : TracksFragments = TracksFragments()

        if (bundle != null) {
            position = bundle.getInt("song-index",0)
            when(bundle.getString("class-name")){
                "TrackListAdapter"->{
                    playerMusicList.addAll(parentActivity!!.mMusicInfo)
                    layoutInit()
                    mediaPlayerInit()
                }
            }
        }

        play.setOnClickListener{

            when(isPlaying){
                true -> pauseBtn()
                false-> playBtn()
            }

        }
        next.setOnClickListener{ changeSong(true)}
        previous.setOnClickListener {changeSong(false)}
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if(p2)
                    parentActivity!!.mediaPlayer!!.seekTo(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) = Unit

            override fun onStopTrackingTouch(p0: SeekBar?)=Unit
        })


    }

    private fun mediaPlayerInit() {
        try{
            if(parentActivity!!.mediaPlayer == null)
                parentActivity!!.mediaPlayer = MediaPlayer()
            parentActivity!!.mediaPlayer!!.reset()
            parentActivity!!.mediaPlayer!!.setDataSource(playerMusicList[position].path)
            parentActivity!!.mediaPlayer!!.prepare()
            parentActivity!!.mediaPlayer!!.start()
                playBtn()
                currentTimeStamp.text = formatDuration(parentActivity!!.mediaPlayer!!.currentPosition.toLong())
                totalTimeStamp.text = formatDuration((parentActivity!!.mediaPlayer!!.duration.toLong()))
                seekBar.progress = 0
                seekBar.max = parentActivity!!.mediaPlayer!!.duration
        }catch (e:Exception){return}

        seekBarSetup()

    }

    private fun playBtn() {
        isPlaying = true
        play.setImageResource(R.drawable.ic_baseline_pause_24)
        parentActivity!!.mediaPlayer!!.start()
    }

    private fun pauseBtn(){
        isPlaying= false
        play.setImageResource((R.drawable.ic_baseline_play_button_100))
        parentActivity!!.mediaPlayer!!.pause()
    }

    private fun changeSong(change: Boolean){
        if(change) {
            if(playerMusicList.size-1 == position)
                position=0
            else {
                ++position
                layoutInit()
                mediaPlayerInit()
            }
        }else{
            if(0 == position)
                position= playerMusicList.size-1
            else {
                --position
                layoutInit()
                mediaPlayerInit()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    private fun layoutInit(){
        Glide.with(requireContext())
            .load(playerMusicList[position].artURI)
            .apply(RequestOptions().placeholder(R.drawable.ic_default_music_red))
            .into(songCover)
        artistName.text = playerMusicList[position].artistName
        songName.text = playerMusicList[position].songName

    }

    private fun seekBarSetup(){
        runnable = Runnable {

            currentTimeStamp.text = formatDuration(parentActivity!!.mediaPlayer!!.currentPosition.toLong())
            seekBar.progress = parentActivity!!.mediaPlayer!!.currentPosition
            Handler(Looper.getMainLooper()).postDelayed(runnable,200)

        }
        Handler(Looper.getMainLooper()).postDelayed(runnable,0)

    }

    override fun onDestroy() {
        super.onDestroy()
        bottomNavbar.visibility = View.VISIBLE
    }

}

