package com.example.spacedestroyer.scenes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.content.SharedPreferences
import com.example.spacedestroyer.MainActivity

class GameOverScene(private val screen: MainActivity.Screen, private val finalScore: Int) : Scene {
    private val paint = Paint().apply {
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        textSize = 64f
        isFakeBoldText = true
    }
    private val sharedPreferences: SharedPreferences = screen.context.getSharedPreferences("high_scores", Context.MODE_PRIVATE)
    private var highScores = mutableListOf<Int>()

    init {
        loadHighScores()
        addNewScore(finalScore)
        saveHighScores()
    }

    private fun loadHighScores() {
        highScores.clear()
        for (i in 0 until 5) {
            highScores.add(sharedPreferences.getInt("high_score_$i", 0))
        }
    }

    private fun saveHighScores() {
        val editor = sharedPreferences.edit()
        for (i in highScores.indices) {
            editor.putInt("high_score_$i", highScores[i])
        }
        editor.apply()
    }

    private fun addNewScore(score: Int) {
        highScores.add(score)
        highScores.sortDescending()
        if (highScores.size > 5) {
            highScores = highScores.subList(0, 5)
        }
    }

    override fun update(et: Float) {}

    override fun render(canvas: Canvas) {
        canvas.drawColor(Color.BLACK)
        canvas.drawText("Game Over", screen.width / 2f, screen.height / 3f, paint)
        canvas.drawText("Your Score: $finalScore", screen.width / 2f, screen.height / 3f + 100, paint)

        for (i in highScores.indices) {
            canvas.drawText("Top ${i + 1}: ${highScores[i]}", screen.width / 2f, (screen.height / 2f) + 100 + i * 70, paint)
        }
    }

    override fun onTouch(e: MotionEvent): Boolean {
        if (e.action == MotionEvent.ACTION_DOWN) {
            screen.scene = StartScene(screen)  // Restart the game on touch
            return true
        }
        return false
    }

    override fun onResume() {}
    override fun onPause() {}
    override fun onDestroy() {}
}
