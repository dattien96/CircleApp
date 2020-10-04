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
import kotlin.random.Random

class Santas constructor(private val displayMetrics: DisplayMetrics) : BaseObject3D() {

    override val objectId: Int
        get() = ITEM_SANTA_ID

    override val startPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 3f, -500f)

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 3f, displayMetrics.heightPixels / 3f)

    override val itemType: ItemType
        get() = ItemType.SANTA

    override val bitMapRes: Int
        get() = R.drawable.ic_santa

    override fun getAnimation(
        pointSprite: Object3D, onAnimationEnd: () -> Unit,
        cubicBezierCurve3D: CubicBezierCurve3D?
    ) = AnimationQueue().apply {
        addAnimation(SplineTranslateAnimation3D(cubicBezierCurve3D).apply {
            durationMilliseconds = 2000
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
        val axis = Vector3(1.0, 0.0, 4.0)
        axis.normalize()
        addAnimation(RotateOnAxisAnimation(axis, 30.0).apply {
            durationMilliseconds = 500
            transformable3D = pointSprite
            repeatMode = Animation.RepeatMode.REVERSE_INFINITE
//            setRepeatCount(4)
            interpolator = AccelerateInterpolator()
        })
        addAnimation(AnimationGroup().apply {
            addAnimation(ScaleAnimation3D(Vector3(0.3, 0.3, 1.0)).apply {
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
            addAnimation(TranslateAnimation3D(Vector3(0.0, 3.0, 0.0)).apply {
                durationMilliseconds = 400
                transformable3D = pointSprite
            })
        })
    }
}