package com.example.spacedestroyer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.SoundPool

class Bullet(private var context: Context, private var x: Float, private var y: Float) {
    private val speed = 1f
    private val radius = 10f
    private lateinit var soundPool: SoundPool
    private var soundId = 0
    private var isSoundLoaded = false

    init {
        initializeSoundPool()
        loadSound("laser_shot.mp3")
    }

    fun update(et: Float) {
        y -= speed * et
    }

    fun render(canvas: Canvas, paint: Paint) {
        canvas.drawCircle(x, y, radius, paint)
    }

    fun isOffScreen(): Boolean {
        return y + radius < 0
    }


    private fun initializeSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()


        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) {
                isSoundLoaded = true
                playSound()
            }
        }
    }


    private fun loadSound(filename: String) {
        try {
            val afd = context.assets.openFd(filename)
            soundId = soundPool.load(afd, 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playSound() {
        if (isSoundLoaded) {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    fun getRect(): Rect {
        return Rect(
            (x - radius).toInt(),
            (y - radius).toInt(),
            (x + radius).toInt(),
            (y + radius).toInt()
        )
    }

    fun releaseSoundPool() {
        soundPool.release()
    }
}
