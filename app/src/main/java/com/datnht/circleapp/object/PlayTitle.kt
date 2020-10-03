package com.datnht.circleapp.`object`

import android.util.DisplayMetrics
import android.view.animation.OvershootInterpolator
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.ItemType
import org.rajawali3d.Object3D
import org.rajawali3d.animation.*
import org.rajawali3d.curves.CubicBezierCurve3D

class PlayTitle constructor(private val displayMetrics: DisplayMetrics): BaseObject3D() {

    override val objectId: Int
        get() = ITEM_TREE_ID
    override val startPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 2f, -500f
        )

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 2f, displayMetrics.heightPixels * 1f / 10f
        )

    override val itemType: ItemType
        get() = ItemType.PLAY_TITLE

    override val bitMapRes: Int
        get() = R.drawable.play_title

    override fun getAnimation(
        pointSprite: Object3D, onAnimationEnd: () -> Unit,
        cubicBezierCurve3D: CubicBezierCurve3D?
    ) = AnimationQueue().apply {
        addAnimation(SplineTranslateAnimation3D(cubicBezierCurve3D).apply {
            durationMilliseconds = 100
            transformable3D = pointSprite
            interpolator = OvershootInterpolator()
            registerListener(object : IAnimationListener {
                override fun onAnimationRepeat(animation: Animation?) = Unit

                override fun onAnimationEnd(animation: Animation?) {
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