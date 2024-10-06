package com.example.spacedestroyer

import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent

class GameScene(private val screen: MainActivity.Screen): Scene {
    private val screenWidth = screen.context.resources.displayMetrics.widthPixels
    private val screenHeight = screen.context.resources.displayMetrics.heightPixels
    private val ship: Spaceship = Spaceship(screen.context, (screenWidth / 2).toFloat(), screenHeight.toFloat())
    private val title: GameObject = Title(screen.width/2f, 250f)
    private var goArray: MutableList<GameObject> = mutableListOf()

    init {
        goArray.add(ship)
        goArray.add(title)
    }

    override fun update(et: Float) {
        for (go in goArray) {
            go.update(et)
        }
    }

    override fun render(canvas: Canvas) {
        for (go in goArray) {
            go.render(canvas)
        }
    }

    override fun onTouch(e: MotionEvent): Boolean {
        for (go in goArray) {
            go.handleEvent(e.action, e.x, e.y)
        }

        return true
    }

    override fun onResume() {}

    override fun onPause() {}
}
