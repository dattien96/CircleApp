package com.datnht.circleapp

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.MotionEvent
import org.rajawali3d.view.SurfaceView

class FirePushView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    SurfaceView(context, attrs) {

    private val renderer = FirePushRender(context)

    init {
        attrs?.also {
            context.obtainStyledAttributes(it, R.styleable.FirePushView).apply {
                val xMax =
                    getFloat(R.styleable.FirePushView_x_max, FirePushRender.DEFAULT_CONFIG.xMax)
                val sizeCoeff = getFloat(
                    R.styleable.FirePushView_size_coeff,
                    FirePushRender.DEFAULT_CONFIG.sizeCoeff
                )
                val timeCoeff = getFloat(
                    R.styleable.FirePushView_floating_time_coeff,
                    FirePushRender.DEFAULT_CONFIG.floatingTimeCoeff
                )

                applyConfig(
                    getConfig().copy(
                        xMax = xMax,
                        sizeCoeff = sizeCoeff,
                        floatingTimeCoeff = timeCoeff,
                        displayMetrics = resources.displayMetrics
                    )
                )

                recycle()
            }


            setFrameRate(60.0)
            setZOrderOnTop(true)
            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            holder.setFormat(PixelFormat.TRANSLUCENT)
            setTransparent(true)
            setSurfaceRenderer(renderer)
        }
    }

    /**
     * Clear all fire object on view
     */
    fun clearView() {
        renderer.clearChildrenScene()
    }

    @SuppressLint("CheckResult")
    @Synchronized
    @JvmOverloads
    fun playAnimation(id: Int, startPoint: ArrayList<Float>, endPoint: ArrayList<Float>,
                      flyEndCallBack: FirePushRender.EndFlyAnimation? = null, itemType: ItemType) {
        val model = Model(
            id,                         // Unique ID of this image, used for Rajawali materials caching
            BitmapFactory.decodeResource(
                resources,
                if (itemType == ItemType.LEFT_PLANE) R.drawable.plane_from_left else R.drawable.plane
            )
        )

        val height =
            (model.bitmap.height.toFloat() / (model.bitmap.width.toFloat() / HEART_WIDTH.toFloat())).toInt()
        val resultBitmap = Bitmap.createScaledBitmap(model.bitmap, HEART_WIDTH, height, true)

        renderer.emitItem(
            resultBitmap,
            itemType,
            HEART_WIDTH,
            height,
            model.id,
            MAX_Y_FULL,
            startPoint,
            endPoint,
            flyEndCallBack
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        renderer.onTouchEvent(event)
        return false
    }

    private fun getConfig() = renderer.getConfig()

    private fun applyConfig(config: FirePushRender.Config) = renderer.applyConfig(config)

    data class Model(val id: Int, val bitmap: Bitmap, val intervalEmit: Int = 300)

    companion object {

        /**
         * Magic param, calculated by experience.
         */
        const val MAX_Y_FULL = 1.5f

        /**
         * Another magic param. Bigger -> better (affects the quality of flying images).
         */
        private const val HEART_WIDTH = 100
    }

}
