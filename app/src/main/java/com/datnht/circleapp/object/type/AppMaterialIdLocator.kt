package com.datnht.circleapp.`object`.type

import com.datnht.circleapp.`object`.BaseObject3D
import com.datnht.circleapp.newsource.GameCoordinate
import com.datnht.circleapp.newsource.StartCoordinate
import com.datnht.circleapp.newsource.SuccessCoordinate

object AppMaterialIdLocator {
    val objectIDs = mutableListOf(
        BaseObject3D.ITEM_PLANE_LEFT_ID,
        BaseObject3D.ITEM_PLANE_RIGHT_ID,
        BaseObject3D.ITEM_REINDEER_ID,
        BaseObject3D.ITEM_REINDEER_INTREE_ID,
        BaseObject3D.ITEM_SANTA_ID,
        BaseObject3D.ITEM_SNOWMAN_INTREE_ID,
        BaseObject3D.ITEM_SNOWMAN_LEFT_ID,
        BaseObject3D.ITEM_SNOWMAN_RIGHT_ID,
        BaseObject3D.ITEM_BIRD_LEFT_ID,
        BaseObject3D.ITEM_BIRD_RIGHT_ID,
        BaseObject3D.ITEM_BIRD_INTREE_ID,
    )

    val objectIDsWithTree = mutableListOf(
        BaseObject3D.ITEM_PLANE_LEFT_ID,
        BaseObject3D.ITEM_PLANE_RIGHT_ID,
        BaseObject3D.ITEM_REINDEER_ID,
        BaseObject3D.ITEM_REINDEER_INTREE_ID,
        BaseObject3D.ITEM_SANTA_ID,
        BaseObject3D.ITEM_SNOWMAN_INTREE_ID,
        BaseObject3D.ITEM_SNOWMAN_LEFT_ID,
        BaseObject3D.ITEM_SNOWMAN_RIGHT_ID,
        BaseObject3D.ITEM_BIRD_LEFT_ID,
        BaseObject3D.ITEM_BIRD_RIGHT_ID,
        BaseObject3D.ITEM_BIRD_INTREE_ID,
        BaseObject3D.ITEM_TREE_ID,
        BaseObject3D.ITEM_PLAY_TITLE_ID
    )

    val objectAnimGroup1 = mutableListOf(
        BaseObject3D.ITEM_PLANE_LEFT_ID,
        BaseObject3D.ITEM_REINDEER_INTREE_ID,
        BaseObject3D.ITEM_SNOWMAN_RIGHT_ID,
        BaseObject3D.ITEM_BIRD_LEFT_ID,
        BaseObject3D.ITEM_TREE_ID,
        BaseObject3D.ITEM_PLAY_TITLE_ID
    )

    val objectAnimGroup2 = mutableListOf(
        BaseObject3D.ITEM_PLANE_RIGHT_ID,
        BaseObject3D.ITEM_BIRD_RIGHT_ID,
        BaseObject3D.ITEM_SNOWMAN_INTREE_ID,
    )

    val objectAnimGroup3 = mutableListOf(
        BaseObject3D.ITEM_SNOWMAN_LEFT_ID,
        BaseObject3D.ITEM_REINDEER_ID,
        BaseObject3D.ITEM_BIRD_INTREE_ID,
    )

    val root = mutableListOf<GameCoordinate>()
    val sucessRoot = mutableListOf<SuccessCoordinate>()
    var startRoot : StartCoordinate? = null
}