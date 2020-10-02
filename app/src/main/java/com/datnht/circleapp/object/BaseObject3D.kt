package com.datnht.circleapp.`object`

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import com.datnht.circleapp.ItemType
import org.rajawali3d.animation.AnimationGroup
import org.rajawali3d.curves.CubicBezierCurve3D
import org.rajawali3d.primitives.PointSprite

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
    }

    abstract val objectId: Int
    abstract val startPoint: ArrayList<Float>
    abstract val endPoint: ArrayList<Float>
    abstract val itemType: ItemType
    abstract val bitMapRes: Int

    fun getAnimationCoordinates(): CubicBezierCurve3D? = null

    fun getBitmap(resources: Resources): Bitmap = BitmapFactory.decodeResource(
        resources,
        bitMapRes
    )

    abstract fun getAnimation(
        pointSprite: PointSprite, onAnimationEnd: () -> Unit,
        cubicBezierCurve3D: CubicBezierCurve3D? = null
    ): AnimationGroup
}