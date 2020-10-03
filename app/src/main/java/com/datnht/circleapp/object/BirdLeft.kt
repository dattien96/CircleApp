package com.datnht.circleapp.`object`

import android.util.DisplayMetrics
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.ItemType
class BirdLeft constructor(private val displayMetrics: DisplayMetrics) : BaseObject3D() {

    override val objectId: Int
        get() = ITEM_BIRD_LEFT_ID

    override val startPoint: ArrayList<Float>
        get() = arrayListOf(0f, displayMetrics.heightPixels / 3f)

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(0f, displayMetrics.heightPixels / 3f)

    override val itemType: ItemType
        get() = ItemType.BIRD_LEFT

    override val bitMapRes: Int
        get() = R.drawable.bird

    override fun getRotateX(): Double = 30.0

    override fun isLeftAnimation() = true
}