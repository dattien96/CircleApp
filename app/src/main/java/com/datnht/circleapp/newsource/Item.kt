package com.datnht.circleapp.newsource

enum class ItemJsonType {
    SANTA, SNOW_MAN, BIRD, REINDEER, PLANE, START, START_BACK, START_GIFT, PLAY_AGAIN, CHANGE_GIFT
}
data class Item constructor(
    val type: ItemJsonType,
    val letfTopCoor: MutableList<Float>,
    val rightBotCoor: MutableList<Float>
){
}