package com.datnht.circleapp.`object`

import android.util.DisplayMetrics
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import com.datnht.circleapp.ItemType
import com.datnht.circleapp.R
import org.rajawali3d.animation.*
import org.rajawali3d.curves.CubicBezierCurve3D
import org.rajawali3d.math.vector.Vector3
import org.rajawali3d.primitives.PointSprite


class SnowManInTree constructor(private val displayMetrics: DisplayMetrics) : BaseObject3D() {

    override val objectId: Int
        get() = ITEM_SNOWMAN_INTREE_ID

    override val startPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 3f, displayMetrics.heightPixels * 8f / 10f
        )

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 3f, displayMetrics.heightPixels * 8f / 10f
        )

    override val itemType: ItemType
        get() = ItemType.SNOWMAN_INTREE

    override val bitMapRes: Int
        get() = R.drawable.snowman

    override fun getAnimation(
        pointSprite: PointSprite, onAnimationEnd: () -> Unit,
        cubicBezierCurve3D: CubicBezierCurve3D?
    ) = AnimationQueue().apply {
        addAnimation(SplineTranslateAnimation3D(cubicBezierCurve3D).apply {
            durationMilliseconds = 100
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
        addAnimation(RotateOnAxisAnimation(Vector3.Axis.Z, 30.0).apply {
            durationMilliseconds = 1500
            transformable3D = pointSprite
            setRepeatCount(3)
//            repeatMode = Animation.RepeatMode.REVERSE
//            interpolator = BounceInterpolator()
        })
        addAnimation(ScaleAnimation3D(Vector3(0.3, 0.3, 0.0)).apply {
            durationMilliseconds = 100
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