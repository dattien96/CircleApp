package com.datnht.circleapp.newsource

enum class ItemJsonType {
    SANTA, SNOW_MAN, BIRD, REINDEER, PLANE
}
data class Item constructor(
    val type: ItemJsonType,
    val letfTopCoor: MutableList<Float>,
    val rightBotCoor: MutableList<Float>
){
}