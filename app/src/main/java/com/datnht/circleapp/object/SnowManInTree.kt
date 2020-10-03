package com.datnht.circleapp.`object`

import android.util.DisplayMetrics
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.ItemType

class SnowManInTree constructor(private val displayMetrics: DisplayMetrics) : BaseObject3D() {

    override val objectId: Int
        get() = ITEM_SNOWMAN_INTREE_ID

    override val startPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 3f, displayMetrics.heightPixels * 6f / 10f
        )

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 3f, displayMetrics.heightPixels * 6f / 10f
        )

    override val itemType: ItemType
        get() = ItemType.SNOWMAN_INTREE

    override val bitMapRes: Int
        get() = R.drawable.snowman

    override fun isLeftRightAnimation() = false
}