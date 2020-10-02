package com.datnht.circleapp.`object`

import android.view.animation.OvershootInterpolator
import com.datnht.circleapp.ItemType
import com.datnht.circleapp.R
import org.rajawali3d.animation.Animation
import org.rajawali3d.animation.AnimationGroup
import org.rajawali3d.animation.IAnimationListener
import org.rajawali3d.animation.SplineTranslateAnimation3D
import org.rajawali3d.curves.CubicBezierCurve3D
import org.rajawali3d.primitives.PointSprite

class Tree: BaseObject3D() {
    override val objectId: Int
        get() = ITEM_TREE_ID
    override val startPoint: ArrayList<Float>
        get() = arrayListOf()
    override val endPoint: ArrayList<Float>
        get() = arrayListOf()
    override val itemType: ItemType
        get() = ItemType.TREE
    override val bitMapRes: Int
        get() = R.drawable.tree

    override fun getAnimation(
        pointSprite: PointSprite, onAnimationEnd: () -> Unit,
        cubicBezierCurve3D: CubicBezierCurve3D?
    ) = AnimationGroup()
}