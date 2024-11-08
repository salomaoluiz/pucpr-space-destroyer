package com.example.spacedestroyer.entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import com.example.spacedestroyer.bullets.Bullet
import kotlin.math.sqrt

class Spaceship(private val context: Context, var x: Float, private var y: Float): GameObject {

    companion object {
        const val WIDTH = 16
        const val HEIGHT = 24
        const val SCALE = 10
        const val SHOOT_INTERVAL = 500L
    }

    private var imageBitmap: Bitmap? = null
    private var src = Rect()
    private var dst = Rect()
    private val vel = 2f
    private var dir = 1
    private var frame = 0
    private val screenWidth = context.resources.displayMetrics.widthPixels
    private var targetX = x
    private var targetY = y
    private val bullets = mutableListOf<Bullet>()

    private var bulletSpeed = 1f
    private var bulletFrequency = 1
    private var lastShotTime = 0L
    init {
        imageBitmap = loadBitmap("ship.png")
    }

    override fun update(et: Float) {
        frame = (0..1).random()
        val distanceX = targetX - x
        val distanceY = targetY - y
        val distance = sqrt((distanceX * distanceX + distanceY * distanceY).toDouble()).toFloat()
        if (distance > 100f) {
            val directionX = distanceX / distance
            val directionY = distanceY / distance
            x += directionX * vel * et
            y += directionY * vel * et
        } else {
            x = targetX
            y = targetY
        }

        bullets.forEach { it.update(et) }
        bullets.removeAll { it.isOffScreen() }

        src.set(dir * WIDTH, frame * HEIGHT, dir * WIDTH + WIDTH, frame * HEIGHT + HEIGHT)
        dst.set(
            (x - WIDTH / 2 * SCALE).toInt(),
            (y - HEIGHT / 2 * SCALE).toInt(),
            (x + WIDTH / 2 * SCALE).toInt(),
            (y + HEIGHT / 2 * SCALE).toInt()
        )
    }

    override fun render(canvas: Canvas) {
        val ship = imageBitmap ?: return
        canvas.drawBitmap(ship, src, dst, null)

        bullets.forEach { it.render(canvas) }
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if(event == MotionEvent.ACTION_MOVE) {
            shoot()
            dir = ((x / screenWidth.toFloat()) * 5).toInt().coerceIn(0, 4)
            val verticalOffset = 50f
            targetX = x
            targetY = y - verticalOffset
        }
    }

    private fun shoot() {
        val currentTime = SystemClock.elapsedRealtime()
        if (currentTime - lastShotTime >= SHOOT_INTERVAL - bulletFrequency * 10 ) {

            bullets.add(Bullet(context, x, y, bulletSpeed))
            lastShotTime = currentTime
        }
    }

    private fun loadBitmap(file: String): Bitmap? {
        try {
            val inputStream = context.assets.open(file)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            return bitmap
        }
        catch (e: Exception) {
            Log.d("App", e.message ?: "Algo ocorreu de errado ao carregar a imagem")
        }

        return null
    }

    fun getBullets(): MutableList<Bullet> {
        return bullets
    }
    fun increaseBulletsFrequency() {
        if(bulletFrequency < 30) {
            bulletFrequency += 1
        }
    }

    fun increaseBulletSpeed() {
        if(bulletSpeed < 10) {
            bulletSpeed += 0.5f
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
