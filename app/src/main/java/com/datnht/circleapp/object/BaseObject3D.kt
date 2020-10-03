package com.datnht.circleapp.`object`

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import com.datnht.circleapp.`object`.type.ItemType
import org.rajawali3d.Object3D
import org.rajawali3d.animation.*
import org.rajawali3d.curves.CubicBezierCurve3D
import org.rajawali3d.math.vector.Vector3
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.random.Random

abstract class BaseObject3D {

    companion object {
        const val ITEM_PLANE_LEFT_ID = 1
        const val ITEM_PLANE_RIGHT_ID = 2
        const val ITEM_TREE_ID = 3
        const val ITEM_SANTA_ID = 4
        const val ITEM_SNOWMAN_LEFT_ID = 5
        const val ITEM_SNOWMAN_RIGHT_ID = 6
        const val ITEM_SNOWMAN_INTREE_ID = 7
        const val ITEM_REINDEER_ID = 8
        const val ITEM_REINDEER_INTREE_ID = 9
        const val ITEM_BIRD_LEFT_ID = 10
        const val ITEM_BIRD_RIGHT_ID = 11
        const val ITEM_BIRD_INTREE_ID = 12
        const val ITEM_PLAY_TITLE_ID = 13
    }

    abstract val objectId: Int
    abstract val startPoint: ArrayList<Float>
    abstract val endPoint: ArrayList<Float>
    abstract val itemType: ItemType
    abstract val bitMapRes: Int

    open fun getRotateX(): Double = 0.0
    open fun isLeftRightAnimation(): Boolean = true
    open fun isLeftAnimation(): Boolean = true

    fun getBitmap(resources: Resources): Bitmap = BitmapFactory.decodeResource(
        resources,
        bitMapRes
    ).apply {
        val out = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 70, out)
        BitmapFactory.decodeStream(ByteArrayInputStream(out.toByteArray()))
    }

    /**
     * Base animation for shake
     */
    open fun getAnimation(
        pointSprite: Object3D, onAnimationEnd: () -> Unit,
        cubicBezierCurve3D: CubicBezierCurve3D? = null
    ): AnimationGroup = AnimationQueue().apply {
        pointSprite.rotX = getRotateX()
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
        val axis = Vector3(0.0, 0.0, 4.0)
        axis.normalize()
        addAnimation(RotateOnAxisAnimation(axis, 30.0).apply {
            var random = Random(4)
            durationMilliseconds = random.nextLong(200, 500)
            transformable3D = pointSprite
            repeatMode = Animation.RepeatMode.REVERSE
            setRepeatCount(random.nextInt(2, 6))
            interpolator = AccelerateInterpolator()
        })
        addAnimation(AnimationGroup().apply {
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
            if (isLeftRightAnimation()) {
                var xCoor = if (isLeftAnimation()) -3.0 else 3.0
                addAnimation(TranslateAnimation3D(Vector3(xCoor, 0.0, 0.0)).apply {
                    durationMilliseconds = 400
                    transformable3D = pointSprite
                })
            }
        })
    }
}