package com.example.spacedestroyer.scenes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.spacedestroyer.R

class Background(context: Context, private val screenWidth: Int, private val screenHeight: Int) {
    private val bitmap: Bitmap = BitmapFactory.decodeResource(context.resources, R.raw.space_background)
    private val scaledBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight * 2, false)
    private var y1 = 0f
    private var y2 = -screenHeight.toFloat()
    private val speed = 5f

    fun update() {
        y1 += speed
        y2 += speed
        if (y1 >= screenHeight) {
            y1 = y2 - screenHeight
        }
        if (y2 >= screenHeight) {
            y2 = y1 - screenHeight
        }
    }

    fun render(canvas: Canvas) {
        canvas.drawBitmap(scaledBitmap, 0f, y1, null)
        canvas.drawBitmap(scaledBitmap, 0f, y2, null)
    }
}
