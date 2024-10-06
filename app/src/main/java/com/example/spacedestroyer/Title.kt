package com.example.spacedestroyer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import java.text.NumberFormat
import java.util.Locale

class Title(private var x: Float, private var y: Float) : GameObject {

    private val paint = Paint()
    private var showTitle = true
    private var points = 0
    private val numberFormat = NumberFormat.getNumberInstance(Locale("pt", "BR"))  // Use Brazilian locale for formatting

    init {
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 65f
        paint.isFakeBoldText = true

        numberFormat.isGroupingUsed = true
        numberFormat.minimumIntegerDigits = 3
    }

    fun addPoint(newPoints: Int) {
        points += newPoints;
    }

    override fun update(et: Float) {}

    override fun render(canvas: Canvas) {
        canvas.drawText("Destrua todos os inimigos", x, y, paint)
        val formattedPoints = numberFormat.format(points)
        canvas.drawText("Pontos: $formattedPoints", x, y + 100, paint)
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {}
}
