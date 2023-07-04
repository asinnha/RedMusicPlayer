package com.example.red_musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashScreen : AppCompatActivity() {

    lateinit var musicVinylDiskIcon:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        musicVinylDiskIcon = findViewById(R.id.musicVinylDiskIcon)

        val animRotate = AnimationUtils.loadAnimation(this,R.anim.rotate)
        musicVinylDiskIcon.startAnimation(animRotate)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        },2000)


    }
}