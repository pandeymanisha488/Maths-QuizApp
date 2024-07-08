package com.quiz.application.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quiz.application.cardSwipe.CardSwipeActivity
import com.quiz.application.databinding.ActivityLevelBinding
import com.quiz.application.model.QuestionDataItem
import com.quiz.application.utils.LoadJsonFromAssets
import com.quiz.application.utils.setfullscreen

class LevelActivity : AppCompatActivity() {

    lateinit var binding: ActivityLevelBinding
    private lateinit var questionList: ArrayList<QuestionDataItem>
    private var doubleBackToExitPressedOnce = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setfullscreen(this)
        questionList = loadQuestionsFromAssets("question.json")
        binding.BtnEasy.setOnClickListener {
            val intent = Intent(this, CardSwipeActivity::class.java)
            intent.putExtra("type", "easy")
            Log.d("==TAG", "BtnEasy Click: easy")

            startActivity(intent)

        }
        binding.BtnMedium.setOnClickListener {
            val intent = Intent(this, CardSwipeActivity::class.java)
            intent.putExtra("type", "medium")
            startActivity(intent)
            Log.d("==TAG", "BtnMedium Click: medium")

        }
        binding.BtnHard.setOnClickListener {
            val intent = Intent(this, CardSwipeActivity ::class.java)
            intent.putExtra("type", "hard")
            Log.d("==TAG", "BtnHard Click: hard")

            startActivity(intent)
        }


    }

    private fun loadQuestionsFromAssets(filename: String): ArrayList<QuestionDataItem> {
        val jsonStr = LoadJsonFromAssets(filename)
        return Gson().fromJson(jsonStr, object : TypeToken<ArrayList<QuestionDataItem>>() {}.type)
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}