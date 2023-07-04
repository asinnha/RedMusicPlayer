package com.example.red_musicplayer.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class AlbumViewModel(val albumCover: String, val albumName: String, val albumYear: String, val path : String)