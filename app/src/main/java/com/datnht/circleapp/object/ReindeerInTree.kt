package com.datnht.circleapp.`object`

import android.util.DisplayMetrics
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.ItemType

class ReindeerInTree constructor(private val displayMetrics: DisplayMetrics) : BaseObject3D() {

    override val objectId: Int
        get() = ITEM_REINDEER_INTREE_ID

    override val startPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 3f, displayMetrics.heightPixels * 6f / 10f
        )

    override val endPoint: ArrayList<Float>
        get() = arrayListOf(displayMetrics.widthPixels / 3f, displayMetrics.heightPixels * 6f / 10f
        )

    override val itemType: ItemType
        get() = ItemType.REINDEER_IN_TREE

    override val bitMapRes: Int
        get() = R.drawable.reindeer

    override fun isLeftRightAnimation() = false
}