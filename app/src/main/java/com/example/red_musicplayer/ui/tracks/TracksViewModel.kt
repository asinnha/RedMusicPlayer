package com.example.red_musicplayer.ui.tracks

import java.util.concurrent.TimeUnit

data class TracksViewModel(val songName:String,val artistName: String, val duration: Long,val path: String, val artURI:String, val album: String)

fun formatDuration(duration: Long):String{
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
            minutes*TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%02d:%02d", minutes, seconds)
}