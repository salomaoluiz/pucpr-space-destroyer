package com.example.spacedestroyer.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log

class Enemy(private val context: Context, private var x: Float, private var y: Float) : GameObject {

    companion object {
        const val WIDTH = 16
        const val HEIGHT = 16
        const val SCALE = 10
        const val SPEED = 1f
    }

    private var imageBitmap: Bitmap? = null
    private var src = Rect()
    private var dst = Rect()

    init {
        imageBitmap = loadBitmap("enemy_small.png")
    }

    override fun update(et: Float) {
        y += SPEED * et
        val frame = (0..1).random()

        src.set(frame * WIDTH, 0, (frame + 1) * WIDTH, HEIGHT)
        dst.set(
            (x - WIDTH / 2 * SCALE).toInt(),
            (y - HEIGHT / 2 * SCALE).toInt(),
            (x + WIDTH / 2 * SCALE).toInt(),
            (y + HEIGHT / 2 * SCALE).toInt()
        )
    }

    override fun render(canvas: Canvas) {
        val enemyBitmap = imageBitmap ?: return
        canvas.drawBitmap(enemyBitmap, src, dst, null)
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {}

    fun isOffScreen(screenHeight: Int): Boolean {
        return y - HEIGHT * SCALE > screenHeight
    }

    private fun loadBitmap(file: String): Bitmap? {
        return try {
            val inputStream = context.assets.open(file)
            BitmapFactory.decodeStream(inputStream).also { inputStream.close() }
        } catch (e: Exception) {
            Log.e("Enemy", "Failed to load enemy image: ${e.message}")
            null
        }
    }

    override fun getRect(): Rect {
        return Rect(
            (x - WIDTH / 2 * SCALE).toInt(),
            (y - HEIGHT / 2 * SCALE).toInt(),
            (x + WIDTH / 2 * SCALE).toInt(),
            (y + HEIGHT / 2 * SCALE).toInt()
        )
    }
}
