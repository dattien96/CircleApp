package com.datnht.circleapp.render3d

import com.datnht.circleapp.`object`.type.AppMaterialIdLocator.objectAnimGroup1
import com.datnht.circleapp.`object`.type.AppMaterialIdLocator.objectAnimGroup2
import com.datnht.circleapp.`object`.BaseObject3D
import com.datnht.circleapp.`object`.type.AppMaterialIdLocator.objectAnimGroup3
import org.rajawali3d.animation.AnimationGroup
import org.rajawali3d.animation.AnimationQueue
import kotlin.collections.HashMap

fun composeAnimation(
    animMap: List<HashMap<Int, AnimationGroup>>,
    objectIDs: MutableList<Int>
): AnimationGroup {
    var resultAnimation = mutableListOf<List<AnimationGroup>>()
    animMap.forEach { map ->
        var tempIds = mutableListOf<Int>().apply {
            addAll(objectAnimGroup1)
            shuffle()
        }
        for (i in 1..3)
            resultAnimation.add(mutableListOf<AnimationGroup>().apply {
                if (i == 3) {
                    tempIds.forEach {
                        add(map[it] ?: return@forEach)
                    }
                } else {
                    tempIds.subList(0, i).forEach {
                        add(map[it] ?: return@forEach)
                    }
                    tempIds.removeAll(tempIds.subList(0, i))
                }
            })
    }

    animMap.forEach { map ->
        var tempIds = mutableListOf<Int>().apply {
            addAll(objectAnimGroup2)
            shuffle()
        }
        for (i in 1..3)
            resultAnimation.add(mutableListOf<AnimationGroup>().apply {
                if (i == 3) {
                    tempIds.forEach {
                        add(map[it] ?: return@forEach)
                    }
                } else {
                    tempIds.subList(0, i).forEach {
                        add(map[it] ?: return@forEach)
                    }
                    tempIds.removeAll(tempIds.subList(0, i))
                }
            })
    }

    animMap.forEach { map ->
        var tempIds = mutableListOf<Int>().apply {
            addAll(objectAnimGroup3)
            shuffle()
        }
        resultAnimation.add(mutableListOf<AnimationGroup>().apply {
            tempIds.forEach {
                add(map[it] ?: return@forEach)
            }
        })
    }

    return AnimationQueue().apply {
        addAnimation(AnimationGroup().apply {
            addAnimation(animMap[1][BaseObject3D.ITEM_PLAY_TITLE_ID])
            addAnimation(animMap[0][BaseObject3D.ITEM_SANTA_ID])
            addAnimation(animMap[1][BaseObject3D.ITEM_TREE_ID])
        })
        resultAnimation.forEach { anims ->
            this@apply.addAnimation(AnimationGroup().apply {
                anims.forEach {
                    this.addAnimation(it)
                }
            })
        }
    }
}
