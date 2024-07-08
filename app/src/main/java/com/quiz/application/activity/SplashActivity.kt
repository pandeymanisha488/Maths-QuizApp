package com.quiz.application.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.quiz.application.R
import com.quiz.application.databinding.ActivitySplashBinding
import com.quiz.application.utils.setfullscreen

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setfullscreen(this)

        val animation = AnimationUtils.loadAnimation(
            this, R.anim.slide_down
        )
        binding.img.startAnimation(animation)

        // Handler().postDelayed({
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LevelActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.

    }
}