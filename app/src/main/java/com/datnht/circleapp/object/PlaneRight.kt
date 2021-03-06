package com.datnht.circleapp.`object`

import android.util.DisplayMetrics
import android.view.animation.OvershootInterpolator
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.ItemType
import org.rajawali3d.Object3D
import org.rajawali3d.animation.Animation
import org.rajawali3d.animation.AnimationGroup
import org.rajawali3d.animation.IAnimationListener
import org.rajawali3d.animation.SplineTranslateAnimation3D
import org.rajawali3d.curves.CubicBezierCurve3D

class PlaneRight constructor(private val displayMetrics: DisplayMetrics) : BaseObject3D() {

    override val objectId: Int
        get() = ITEM_PLANE_RIGHT_ID

    override val startPoint: ArrayList<Float>
        get() = arrayListOf(
            displayMetrics.widthPixels.toFloat() + 200f,
            displayMetrics.heightPixels / 3f
        )

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(-100f, displayMetrics.heightPixels * 2f / 3f)

    override val itemType: ItemType
        get() = ItemType.RIGHT_PLANE

    override val bitMapRes: Int
        get() = R.drawable.plane

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
                    onAnimationEnd.invoke()
                    animation?.unregisterListener(this)
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
    }
}