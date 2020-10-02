package com.datnht.circleapp

import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.datnht.circleapp.`object`.BaseObject3D.Companion.ITEM_PLANE_LEFT_ID
import com.datnht.circleapp.`object`.BaseObject3D.Companion.ITEM_PLANE_RIGHT_ID
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), FirePushRender.OnCatchItemListener {

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

    private val catchItemRunner: Runnable by lazy {
        Runnable {
            Toast.makeText(this@MainActivity, "catch ", Toast.LENGTH_SHORT).show()
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
        fire_push_view?.setOnCatchItemListener(this)
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
            ITEM_PLANE_LEFT_ID,
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
            ITEM_PLANE_RIGHT_ID,
            arrayListOf(-100f, 300f),
            arrayListOf(
                width.toFloat() + 200f, 1500f
            ),
            itemType = ItemType.LEFT_PLANE
        )
    }

    override fun onCatch(itemType: ItemType) {
        mainHandler?.run {
            post(catchItemRunner)
        }
    }
}