package com.datnht.circleapp.`object`

import android.util.DisplayMetrics
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.ItemType
import org.rajawali3d.Object3D
import org.rajawali3d.animation.*
import org.rajawali3d.curves.CubicBezierCurve3D
import org.rajawali3d.math.vector.Vector3

class Reindeer constructor(private val displayMetrics: DisplayMetrics) : BaseObject3D() {

    override val objectId: Int
        get() = ITEM_REINDEER_ID

    override val startPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels.toFloat(), displayMetrics.heightPixels.toFloat())

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 2f, displayMetrics.heightPixels / 2f
        )

    override val itemType: ItemType
        get() = ItemType.REINDEER

    override val bitMapRes: Int
        get() = R.drawable.reindeer

    override fun getAnimation(
        pointSprite: Object3D, onAnimationEnd: () -> Unit,
        cubicBezierCurve3D: CubicBezierCurve3D?
    ) = AnimationGroup().apply {
        addAnimation(SplineTranslateAnimation3D(cubicBezierCurve3D).apply {
            durationMilliseconds = 3000
            transformable3D = pointSprite
            interpolator = OvershootInterpolator()
            registerListener(object : IAnimationListener {
                override fun onAnimationRepeat(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) {
                }

                override fun onAnimationStart(animation: Animation?) {
                    pointSprite.isVisible = true
                }

                override fun onAnimationUpdate(
                    animation: Animation?,
                    interpolatedTime: Double
                ) {

                }
            })
        })
        val axis = Vector3(0.0, 0.0, 4.0)
        axis.normalize()
        addAnimation(RotateOnAxisAnimation(axis, 15.0).apply {
            durationMilliseconds = 300
            transformable3D = pointSprite
            repeatMode = Animation.RepeatMode.REVERSE
            setRepeatCount(4)
            interpolator = AccelerateInterpolator()
        })
        addAnimation(ScaleAnimation3D(Vector3(0.3, 0.3, 0.0)).apply {
            durationMilliseconds = 1500
            transformable3D = pointSprite
            registerListener(object : IAnimationListener {
                override fun onAnimationRepeat(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) {
                    onAnimationEnd.invoke()
                    animation?.unregisterListener(this)
                }

                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationUpdate(
                    animation: Animation?,
                    interpolatedTime: Double
                ) {

                }
            })
        })
    }
}