package com.example.spacedestroyer.scenes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import com.example.spacedestroyer.MainActivity
import kotlin.random.Random

class StartScene(private val screen: MainActivity.Screen): Scene {

    private val paint = Paint()
    private var controlTime = 0f
    private val highScores: List<Int> = loadHighScores()

    init {
        paint.textSize = 64f
        paint.textAlign = Paint.Align.CENTER
        paint.isFakeBoldText = true
        paint.textSkewX = -0.3f
        paint.color = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
    }

    private fun loadHighScores(): List<Int> {
        val sharedPreferences = screen.context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
        val scores = mutableListOf<Int>()
        for (i in 0 until 5) {
            scores.add(sharedPreferences.getInt("high_score_$i", 0))
        }
        return scores
    }

    override fun update(et: Float) {
        controlTime += et
        if (controlTime > 300) {
            controlTime = 0f
            paint.color = Color.rgb(Random.nextInt(256), 255, Random.nextInt(256))
        }
    }

    override fun render(canvas: Canvas) {
        val text = "Toque para iniciar..."
        canvas.drawText(text, screen.width/2f, screen.height/2f, paint)

        canvas.drawText("Top Scores", screen.width / 2f, screen.height/2f + 250, paint)

        for ((index, score) in highScores.withIndex()) {
            canvas.drawText("Top ${index + 1}: $score", screen.width / 2f, screen.height/2f + 400 + index * 80, paint)
        }

    }

    override fun onTouch(e: MotionEvent): Boolean {
        return when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                screen.scene = GameScene(screen)
                true
            }
            else -> false
        }
    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun onDestroy() {}
}
