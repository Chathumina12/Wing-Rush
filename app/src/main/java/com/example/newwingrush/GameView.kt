package com.example.newwingrush

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.example.newwingrush.GameTask
import com.example.newwingrush.R

class GameView(var c: Context, var gameTask: GameTask) : View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myBirdPosition = 0
    private val otherBirds = ArrayList<HashMap<String, Any>>()

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherBirds.add(map)
        }
        time = time + 10 + speed
        val birdWidth = viewWidth / 5
        val birdHeight = birdWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.bird1, null)

        d.setBounds(
            myBirdPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - birdHeight,
            myBirdPosition * viewWidth / 3 + viewWidth / 15 + birdWidth - 25,
            viewHeight - 2
        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        for (i in otherBirds.indices) {
            try {
                val birdX = otherBirds[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var birdY = time - otherBirds[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.bird2, null)

                d2.setBounds(
                    birdX + 25, birdY - birdHeight, birdX + birdWidth - 25, birdY
                )
                d2.draw(canvas)
                if (otherBirds[i]["lane"] as Int == myBirdPosition) {
                    if (birdY > viewHeight - 2 - birdHeight && birdY < viewHeight - 2) {
                        gameTask.closeGame(score)
                    }
                }
                if (birdY > viewHeight + birdHeight) {
                    otherBirds.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8)
                    if (score > highScore) {
                        highScore = score
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (myBirdPosition > 0) {
                        myBirdPosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myBirdPosition < 2) {
                        myBirdPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }
}
