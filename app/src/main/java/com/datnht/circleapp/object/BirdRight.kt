package com.datnht.circleapp.`object`

import android.util.DisplayMetrics
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.ItemType

class BirdRight constructor(private val displayMetrics: DisplayMetrics) : BaseObject3D() {

    override val objectId: Int
        get() = ITEM_BIRD_RIGHT_ID

    override val startPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels.toFloat(), displayMetrics.heightPixels * 2 / 3f)

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels.toFloat(), displayMetrics.heightPixels * 2 / 3f)

    override val itemType: ItemType
        get() = ItemType.BIRD_RIGHT

    override val bitMapRes: Int
        get() = R.drawable.bird

    override fun getRotateX(): Double = -30.0

    override fun isLeftAnimation() = false
}