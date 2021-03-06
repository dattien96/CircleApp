package com.datnht.circleapp.`object`

import android.util.DisplayMetrics
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.ItemType

class SnowManLeft constructor(private val displayMetrics: DisplayMetrics) : BaseObject3D() {

    override val objectId: Int
        get() = ITEM_SNOWMAN_LEFT_ID

    override val startPoint: ArrayList<Float>
        get() = arrayListOf(0f, displayMetrics.heightPixels / 2f)

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(0f, displayMetrics.heightPixels / 2f)

    override val itemType: ItemType
        get() = ItemType.SNOWMAN_LEFT

    override val bitMapRes: Int
        get() = R.drawable.snowman

    override fun getRotateX(): Double = 30.0

    override fun isLeftAnimation() = true
}