package com.example.spacedestroyer.scenes

import android.graphics.Canvas
import android.graphics.Rect
import android.media.MediaPlayer
import android.view.MotionEvent
import com.example.spacedestroyer.*
import com.example.spacedestroyer.bullets.Bullet
import com.example.spacedestroyer.bullets.BulletFrequencyPowerUp
import com.example.spacedestroyer.bullets.BulletSpeedPowerUp
import com.example.spacedestroyer.entities.Enemy
import com.example.spacedestroyer.entities.GameObject
import com.example.spacedestroyer.entities.Spaceship
import com.example.spacedestroyer.entities.Title
import kotlin.random.Random

class GameScene(private val screen: MainActivity.Screen) : Scene {
    private val screenWidth = screen.context.resources.displayMetrics.widthPixels
    private val screenHeight = screen.context.resources.displayMetrics.heightPixels
    private val ship: Spaceship = Spaceship(screen.context, (screenWidth / 2).toFloat(), screenHeight.toFloat())
    private val title: Title = Title(screen.context, screen.width / 2f, 250f)
    private var powerUps: MutableList<GameObject> = mutableListOf()
    private var goArray: MutableList<GameObject> = mutableListOf()
    private var mediaPlayer: MediaPlayer? = null
    private var enemies: MutableList<Enemy> = mutableListOf()
    private var spawnTime = 0f
    private var speedMultiplier = 1f
    private var lastPowerUpScore = 0

    init {
        initializeMediaPlayer()
        goArray.add(ship)
        goArray.add(title)
        title.onLevelAdvance = {
            enemies.removeAll(enemies)
            spawnRandomPowerUp()
        }
    }

    private fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer.create(screen.context, R.raw.background_music)
        mediaPlayer?.apply {
            isLooping = true
            setVolume(0.1f, 0.1f)
            start()
        }
    }


    override fun update(et: Float) {
        spawnTime += et
        if (spawnTime > 1000) {
            spawnEnemy()
            spawnTime = 0f
        }

        goArray.forEach { it.update(et) }
        enemies.forEach { it.update(et * speedMultiplier) }
        checkCollisions()

        if (title.points >= lastPowerUpScore + 20) {
            spawnRandomPowerUp()
            lastPowerUpScore = title.points
        }
        powerUps.forEach { it.update(et) }
        checkPowerUpCollection()
        title.update(et)


    }

    override fun render(canvas: Canvas) {
        goArray.forEach { it.render(canvas) }
        enemies.forEach { it.render(canvas) }
        powerUps.forEach { it.render(canvas) }
    }

    private fun checkCollisions() {
        val bullets = ship.getBullets()

        val bulletsToRemove = mutableListOf<Bullet>()
        val enemiesToRemove = mutableListOf<Enemy>()

        for (bullet in bullets) {
            for (enemy in enemies) {
                if (Rect.intersects(bullet.getRect(), enemy.getRect())) {
                    bulletsToRemove.add(bullet)
                    enemiesToRemove.add(enemy)
                    title.addPoint(1);
                    speedMultiplier += 0.01f * title.level
                }
            }
        }

        for (enemy in enemies) {
            if (enemy.isOffScreen(screenHeight)) {
                enemiesToRemove.add(enemy)
                title.enemyEscape(screen)
            }
        }

        bullets.removeAll(bulletsToRemove)
        enemies.removeAll(enemiesToRemove)
    }

    private fun spawnEnemy() {
        val x = Random.nextFloat() * screenWidth
        enemies.add(Enemy(screen.context, x, 0f))
    }

    override fun onTouch(e: MotionEvent): Boolean {
        for (go in goArray) {
            go.handleEvent(e.action, e.x, e.y)
        }

        return true
    }

    override fun onResume() {
        mediaPlayer?.start()
    }

    override fun onPause() {
        mediaPlayer?.pause()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun spawnRandomPowerUp() {
        val x = Random.nextFloat() * screenWidth
        val minY = screenHeight / 4f
        val maxY = screenHeight / 1.5f

        val y = minY.coerceAtLeast(maxY) + Random.nextFloat() * (minY - maxY)

        val powerUp = if (Random.nextBoolean()) {
            BulletFrequencyPowerUp(screen.context, x, y)
        } else {
            BulletSpeedPowerUp(screen.context, x, y)
        }
        powerUps.add(powerUp)
    }

    private fun checkPowerUpCollection() {
        val shipRect = ship.getRect()
        val collectedPowerUps = mutableListOf<GameObject>()

        for (powerUp in powerUps) {
            if (Rect.intersects(shipRect, powerUp.getRect())) {
                collectedPowerUps.add(powerUp)
                applyPowerUpEffect(powerUp)
            }
        }

        powerUps.removeAll(collectedPowerUps)
    }

    private fun applyPowerUpEffect(powerUp: GameObject) {
        when (powerUp) {
            is BulletFrequencyPowerUp -> ship.increaseBulletsFrequency()
            is BulletSpeedPowerUp -> ship.increaseBulletSpeed()
        }
    }
}
