package com.example.red_musicplayer.ui.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Playlists"
    }
    val text: LiveData<String> = _text
}