package com.datnht.circleapp.newsource

import com.datnht.circleapp.newsource.Item

data class GameCoordinate constructor(
    val msecondStart: Long,
    val msecondEnd: Long,
    val items: MutableList<Item>
)