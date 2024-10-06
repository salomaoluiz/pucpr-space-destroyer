package com.example.spacedestroyer
import android.graphics.Canvas

interface GameObject {

    fun update(et: Float)
    fun render(canvas: Canvas)
    fun handleEvent(event: Int, x: Float, y: Float)

}
