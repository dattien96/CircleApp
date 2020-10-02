package com.datnht.circleapp

import android.annotation.SuppressLint
import android.content.Context
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
                        displayMetrics = resources.displayMetrics,
                        resource = resources
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
    fun playAnimation(flyEndCallBack: FirePushRender.EndFlyAnimation? = null) {
//        val height =
//            (model.bitmap.height.toFloat() / (model.bitmap.width.toFloat() / HEART_WIDTH.toFloat())).toInt()
//        val resultBitmap = Bitmap.createScaledBitmap(model.bitmap, HEART_WIDTH, height, true)

        renderer.emitItem()
    }

    fun setOnCatchItemListener(onRenderListener: FirePushRender.OnRenderListener) {
        renderer?.onRenderListener = onRenderListener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        renderer.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private fun getConfig() = renderer.getConfig()

    private fun applyConfig(config: FirePushRender.Config) = renderer.applyConfig(config)

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
