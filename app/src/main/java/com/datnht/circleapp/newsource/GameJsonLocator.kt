package com.datnht.circleapp.newsource

import android.app.Application
import android.content.Context
import android.graphics.RectF
import android.util.DisplayMetrics
import android.view.WindowManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


object GameJsonLocator {
    const val BASE_WIDTH = 1080f
    val gameCoor = mutableListOf<GameCoordinate>()

    fun convertBaseJsonToDevice(context: Application) {
        val jsonString = getJsonFromAssets(context, "game.json")
        val listType: Type =
            object : TypeToken<List<GameCoordinate>>() {}.type

        val gameList: MutableList<GameCoordinate> = Gson().fromJson(jsonString, listType)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displaymetrics: DisplayMetrics = DisplayMetrics()
        wm.getDefaultDisplay().getMetrics(displaymetrics)
        val width = displaymetrics.widthPixels
        val ratio: Float = width / BASE_WIDTH
        gameCoor.addAll(gameList.apply {
            this.forEach { gameRoot ->
                gameRoot.items.forEach {
                    it.letfTopCoor[0] = it.letfTopCoor[0] * ratio
//                    it.letfTopCoor[1] = it.letfTopCoor[1] * ratio
                    it.rightBotCoor[0] = it.rightBotCoor[0] * ratio
//                    it.rightBotCoor[1] = it.rightBotCoor[1] * ratio
                }
            }
        })
    }

    fun checkPointInRectF(xTouch: Float, yTouch: Float, currentMsec: Long): ItemJsonType? {
        val preItem = gameCoor.findLast { currentMsec in it.msecondStart..it.msecondEnd}
        if (preItem != null) {
            val preIndex = gameCoor.indexOf(preItem)
            var x1: Float
            var y1: Float
            var x2: Float
            var y2: Float
            preItem.items.forEach {
                x1 = it.letfTopCoor[0]
                y1 = it.letfTopCoor[1]
                x2 = it.rightBotCoor[0]
                y2 = it.rightBotCoor[1]
                if (xTouch in x1..x2
                    && yTouch in y1..y2) {
                    return it.type
                }
            }
        }
        return null
    }
}