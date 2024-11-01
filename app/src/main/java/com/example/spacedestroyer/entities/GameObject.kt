package com.example.spacedestroyer.entities
import android.graphics.Canvas
import android.graphics.Rect

interface GameObject {

    fun update(et: Float)
    fun render(canvas: Canvas)
    fun handleEvent(event: Int, x: Float, y: Float)
    fun getRect(): Rect
}
