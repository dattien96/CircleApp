package com.datnht.circleapp

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_congratulation.view.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size


class MainActivity : AppCompatActivity(), FirePushRender.OnRenderListener {

    private var currentItemSelectedType: ItemType? = null

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
        }
    }

    private val catchItemRunner: Runnable by lazy {
        Runnable {
            showSuccessView()
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
        fire_push_view?.setOnCatchItemListener(this)
        layout_success?.btn_play_again?.setOnClickListener {
            layout_success?.visibility = View.GONE
            emitPlane1Animation()
        }

    }

    fun clearView() {
        fire_push_view?.clearView();
    }

    private fun emitPlane1Animation() {
        fire_push_view?.playAnimation(
            flyEndAnimation
        )
    }

    override fun onCatch(itemType: ItemType) {
        currentItemSelectedType = itemType
        mainHandler?.run {
            post(catchItemRunner)
        }
    }

    override fun onInitSenceSuccess() {
        emitPlane1Animation()
    }

    private fun showSuccessView() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        val height = metrics.heightPixels
        val width = metrics.widthPixels
        layout_success?.visibility = View.VISIBLE
        var builder = layout_success.viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.BLUE, Color.WHITE)
            .setDirection(0.0, 359.0)
            .setSpeed(5f, 10f)
            .setFadeOutEnabled(true)
            .setTimeToLive(5000L)
            .addShapes(Shape.Square)
            .addSizes(Size(8), Size(12, 6f))
            .setPosition(
                width / 2f,
                height / 2f + 100f
            )

        builder.burst(400)
        builder.streamFor(200, 3000L)
    }
}