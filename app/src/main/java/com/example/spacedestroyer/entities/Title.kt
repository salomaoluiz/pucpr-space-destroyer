package com.example.spacedestroyer.entities

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.spacedestroyer.MainActivity
import com.example.spacedestroyer.scenes.GameOverScene
import java.text.NumberFormat
import java.util.Locale

class Title(private val context: Context, private var x: Float, private var y: Float) : GameObject {
    private val screenWidth = context.resources.displayMetrics.widthPixels

    private val paint = Paint()
    var points = 0
        private set
    var level = 1
        private set
    private val numberFormat = NumberFormat.getNumberInstance(Locale("pt", "BR"))
    private var levelTime = 10
    private var remainingTime = levelTime
    private var lastUpdateTime = System.currentTimeMillis()
    var onLevelAdvance: (() -> Unit)? = null

    init {
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize =48f
        paint.isFakeBoldText = true

        numberFormat.isGroupingUsed = true
        numberFormat.minimumIntegerDigits = 3
    }

    private fun advanceLevel() {
        level += 1
        levelTime += 5
        remainingTime = levelTime
        onLevelAdvance?.invoke();
    }

    fun enemyEscape(screen: MainActivity.Screen) {
        remainingTime += level
        if(remainingTime > levelTime) {
            screen.scene = GameOverScene(screen, points)
        }
    }

    fun addPoint(newPoints: Int) {
        points += newPoints;
    }

    override fun update(et: Float) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime >= 1000) {
            remainingTime -= 1
            lastUpdateTime = currentTime
            if (remainingTime <= 0) {
                advanceLevel()
            }
        }
    }

    override fun render(canvas: Canvas) {
        val formattedPoints = numberFormat.format(points)
        canvas.drawText("Pontos: $formattedPoints", x, y + 40, paint)
        canvas.drawText("Level: $level", x, y + 80, paint)
        canvas.drawText("Time: $remainingTime s", x, y + 120, paint)
        renderLocationBar(canvas )
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {}

    private fun renderLocationBar(canvas: Canvas) {
        val barHeight = 20
        val barWidth = screenWidth - 100
        val barX = 50f
        val barY = y + 180

        val progress = (1 - remainingTime.toFloat() / levelTime).coerceIn(0f, 1f)

        paint.color = Color.GRAY
        canvas.drawRect(barX, barY, barX + barWidth, barY + barHeight, paint)

        if (progress >= 0.8f) {
            paint.color = Color.GREEN
        } else {
            paint.color = Color.RED
        }
        canvas.drawRect(barX, barY, barX + (progress * barWidth), barY + barHeight, paint)

        paint.color = Color.WHITE
    }

    override fun getRect(): Rect {
        return Rect()
    }
}
