package com.datnht.circleapp

import org.rajawali3d.animation.Animation
import org.rajawali3d.animation.AnimationGroup
import org.rajawali3d.animation.AnimationQueue
import org.rajawali3d.animation.IAnimationListener
import java.util.*

enum class AnimationType {
    SANTA, SNOWMAN, PLANE, REINDEER
}

fun getPlaneAnimation(
    planeLeftAnim: AnimationGroup?,
    planeRightAnim: AnimationGroup?
): AnimationGroup {
    val random = java.util.Random().nextBoolean()
    val startAnim = if (random) planeLeftAnim else planeRightAnim
    val endAnim = if (random) planeRightAnim else planeLeftAnim

    var animGroup = AnimationQueue()
    return animGroup.apply {
        addAnimation(startAnim)
        addAnimation(endAnim)
    }
}

fun getReindeerAnimation(
    reindeerAnim: AnimationGroup?,
    reindeerInTreeAnim: AnimationGroup?
): AnimationGroup {
    val random = java.util.Random().nextBoolean()
    val startAnim = if (random) reindeerAnim else reindeerInTreeAnim
    val endAnim = if (random) reindeerInTreeAnim else reindeerAnim

    return AnimationQueue().apply {
        addAnimation(startAnim)
        addAnimation(endAnim)
    }
}

fun getSnowManAnimation(
    snowManLeftAnimation: AnimationGroup?,
    snowManRightAnim: AnimationGroup?,
    snowManInTreeAnim: AnimationGroup?
): AnimationGroup {
    var anims = mutableListOf(
        snowManInTreeAnim,
        snowManLeftAnimation,
        snowManRightAnim
    ).apply { shuffle() }

    return AnimationQueue().apply {
        anims.forEach {
            addAnimation(it)
        }
    }
}