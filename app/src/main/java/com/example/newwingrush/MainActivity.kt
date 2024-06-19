package com.example.newwingrush

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView

class MainActivity : AppCompatActivity(), GameTask {
    lateinit var rootlayout: RelativeLayout
    lateinit var mGameView: GameView
    lateinit var score: TextView
    lateinit var btnPlay: ImageButton
    lateinit var restartBtn: ImageButton
    lateinit var prefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rootlayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        btnPlay = findViewById(R.id.btnPlay)
        restartBtn = findViewById(R.id.restart)


        mGameView = GameView(this, this)

        prefs = getSharedPreferences("myGamePrefs", MODE_PRIVATE)

        val highScore = prefs.getInt("highScore", 0)
        score.text = "High0est Score: $highScore"

        btnPlay.setOnClickListener {
            mGameView.setBackgroundResource(R.drawable.background)
            rootlayout.addView(mGameView)
            btnPlay.visibility = View.INVISIBLE
            score.visibility = View.INVISIBLE
        }

        restartBtn.setOnClickListener {
            closeGame(0)
        }
    }

    override fun closeGame(mScore: Int) {
        score.text = "HIGH SCORE: ${getHighScore()} \n\nSCORE : $mScore"
        //score.text = "Score : $mScore"
        rootlayout.removeView(mGameView)
        mGameView = GameView(this, this)
        btnPlay.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
        restartBtn.visibility = View.VISIBLE

        storeHighScore(mScore)

    }
    private fun storeHighScore(currentScore: Int){
        val highScore = prefs.getInt("HighScore", 0)
        if(currentScore > highScore) {
            val editor = prefs.edit()
            editor.putInt("highScore", currentScore)
            editor.apply()
        }
    }

    private fun getHighScore() : Int {
        return prefs.getInt("highScore", 0)
    }


}
