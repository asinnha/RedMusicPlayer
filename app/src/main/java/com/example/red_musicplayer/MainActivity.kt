package com.example.red_musicplayer

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.red_musicplayer.databinding.ActivityMainBinding
import com.example.red_musicplayer.ui.tracks.TrackAdapter
import com.example.red_musicplayer.ui.tracks.TracksFragments
import com.example.red_musicplayer.ui.tracks.TracksViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var mMusicInfo: ArrayList<TracksViewModel> = ArrayList()
    var filter: String = MediaStore.Audio.Media.TITLE
    lateinit var trackAdapter : TrackAdapter
    var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestRuntimePermission()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)

    }

    @SuppressLint("Range")
    fun fetchAudio(): ArrayList<TracksViewModel> {

        val tempList = ArrayList<TracksViewModel>()
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )


        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null,
            filter +" ASC", null)
        if (cursor != null) {
            if (cursor.moveToFirst())
                do {
                    val titleC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))?:"Unknown"
                    val idC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))?:"Unknown"
                    val albumC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))?:"Unknown"
                    val artistC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))?:"Unknown"
                    val pathC = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val durationC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val albumIdC = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)).toString()
                    val uri = Uri.parse("content://media/external/audio/albumart")

                    val artUriC = Uri.withAppendedPath(uri, albumIdC).toString()
                    val music = TracksViewModel(titleC,artistC,durationC,pathC,artUriC,albumC)

                    val file = File(music.path)
                    if (file.exists())
                        tempList.add(music)
                } while (cursor.moveToNext())
            cursor.close()
        }
        return tempList
    }

    fun sortingTracks() {

        var arrSortingCriteria = arrayOf("By Name", "By Date Added On", "By Artist")
        val checkedItems = BooleanArray(arrSortingCriteria.size)


        MaterialAlertDialogBuilder(this)
            .setTitle("Sort Tracks :")
            .setMultiChoiceItems(arrSortingCriteria,checkedItems){ dialog, which, checked ->
                checkedItems[which] = checked
                when(arrSortingCriteria[which]){
                    "By Name" -> {
                        filter = MediaStore.Audio.Media.TITLE

                    }
                    "By Date Added On" ->{
                        filter = MediaStore.Audio.Media.DATE_ADDED

                    }
                    "By Artist"->{
                        filter = MediaStore.Audio.Media.ARTIST

                    }
                }
            }
            .setPositiveButton("SORT"){dialog,which ->
                mMusicInfo = fetchAudio()
                trackAdapter.updateTrackList( mMusicInfo)
            }
            .show()
    }


    private fun requestRuntimePermission(){
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED  ){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1)
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==1 || requestCode==2){
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"granted",Toast.LENGTH_SHORT).show()
        }else{
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),2)
        }
    }

}