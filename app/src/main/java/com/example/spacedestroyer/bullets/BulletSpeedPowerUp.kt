package com.example.spacedestroyer.bullets

import android.content.Context
import android.graphics.*
import android.util.Log
import com.example.spacedestroyer.entities.GameObject

class BulletSpeedPowerUp(private val context: Context, private var x: Float, private var y: Float) : GameObject {

    companion object {
        const val WIDTH = 82
        const val HEIGHT = 82
        const val SCALE = 0.7
    }
    private var imageBitmap: Bitmap? = null
    private var src = Rect()
    private var dst = Rect()

    init {
        imageBitmap = loadBitmap("bullet_speed_power_up.png")
    }
    override fun update(et: Float) {}

    override fun render(canvas: Canvas) {
        val speedPowerUpBitmap = imageBitmap ?: return

        dst.set(
            (x - WIDTH * SCALE).toInt(),
            (y - HEIGHT * SCALE).toInt(),
            (x + WIDTH * SCALE).toInt(),
            (y + HEIGHT * SCALE).toInt()
        )
        src.set(0, 0,  WIDTH, HEIGHT)

        canvas.drawBitmap(speedPowerUpBitmap, src, dst, null)


    }

    override fun handleEvent(event: Int, x: Float, y: Float) {}

    override fun getRect(): Rect {
        return Rect(
            (x - WIDTH * SCALE).toInt(),
            (y - HEIGHT * SCALE).toInt(),
            (x + WIDTH * SCALE).toInt(),
            (y + HEIGHT * SCALE).toInt()
        )
    }

    private fun loadBitmap(file: String): Bitmap? {
        return try {
            val inputStream = context.assets.open(file)
            BitmapFactory.decodeStream(inputStream).also { inputStream.close() }
        } catch (e: Exception) {
            Log.e("BulletSpeedPowerUp", "Failed to load bullet speed power up image: ${e.message}")
            null
        }
    }
}
