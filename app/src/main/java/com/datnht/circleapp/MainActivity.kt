package com.datnht.circleapp

import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        const val ITEM_PLANE_1_PUSH_ID = 1;
        const val ITEM_PLANE_2_PUSH_ID = 2;
    }

    /**
     * Get main thread for update ui
     */
    private val mainHandler: Handler? by lazy {
        mainLooper?.let {
            Handler(it)
        }
    }

    /**
     * Runner for free boost local update
     */
    private val localFreeBoostRunner: Runnable by lazy {
        Runnable {
            emitPlane2Animation();
        }
    }

    /**
     * Callback from Gl thread
     */
    private val flyEndAnimation = object : FirePushRender.EndFlyAnimation {
        override fun onEndFly() {
            mainHandler?.run {
                post(localFreeBoostRunner)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_fire?.setOnClickListener {
            emitPlane1Animation();
        }
    }

    fun clearView() {
        fire_push_view?.clearView();
    }

    fun emitPlane1Animation() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y

        fire_push_view?.playAnimation(
            ITEM_PLANE_1_PUSH_ID,
            arrayListOf(width.toFloat() + 200f, 300f),
            arrayListOf(
                -100f, 1500f
            ),
            flyEndAnimation,
            itemType = ItemType.RIGHT_PLANE
        )
    }

    fun emitPlane2Animation() {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y

        fire_push_view?.playAnimation(
            ITEM_PLANE_2_PUSH_ID,
            arrayListOf(-100f, 300f),
            arrayListOf(
                width.toFloat() + 200f, 1500f
            ),
            itemType = ItemType.LEFT_PLANE
        )
    }
}