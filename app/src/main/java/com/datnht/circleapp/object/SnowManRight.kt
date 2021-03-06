package com.datnht.circleapp.`object`

import android.util.DisplayMetrics
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.ItemType

class SnowManRight constructor(private val displayMetrics: DisplayMetrics) : BaseObject3D() {

    override val objectId: Int
        get() = ITEM_SNOWMAN_RIGHT_ID

    override val startPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels.toFloat(), displayMetrics.heightPixels / 2f)

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels.toFloat(), displayMetrics.heightPixels / 2f)

    override val itemType: ItemType
        get() = ItemType.SNOWMAN_RIGHT

    override val bitMapRes: Int
        get() = R.drawable.snowman

    override fun getRotateX(): Double = -30.0

    override fun isLeftAnimation() = false
}