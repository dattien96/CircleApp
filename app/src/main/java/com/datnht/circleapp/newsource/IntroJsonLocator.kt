package com.datnht.circleapp.newsource

import android.app.Application
import android.content.Context
import android.graphics.RectF
import android.util.DisplayMetrics
import android.view.WindowManager
import com.datnht.circleapp.newsource.GameJsonLocator.BASE_WIDTH
import com.google.gson.Gson

object IntroJsonLocator {
    var introCoor: StartCoordinate? = null

    fun convertBaseJsonToDevice(context: Application) {
        val jsonString = getJsonFromAssets(context, "start.json")

        val introObject: StartCoordinate = Gson().fromJson(jsonString, StartCoordinate::class.java)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displaymetrics: DisplayMetrics = DisplayMetrics()
        wm.getDefaultDisplay().getMetrics(displaymetrics)
        val width = displaymetrics.widthPixels
        val ratio: Float = width / BASE_WIDTH
        introCoor = introObject.apply {
            items.forEach {
                it.letfTopCoor[0] = it.letfTopCoor[0] * ratio
                it.letfTopCoor[1] = it.letfTopCoor[1] * ratio
                it.rightBotCoor[0] = it.rightBotCoor[0] * ratio
                it.rightBotCoor[1] = it.rightBotCoor[1] * ratio
            }
        }
    }

    fun checkPointInRectF(xTouch: Float, yTouch: Float): ItemJsonType? {
        if (introCoor != null) {
            var x1: Float
            var y1: Float
            var x2: Float
            var y2: Float
            var rectF: RectF
            introCoor!!.items.forEach {
                x1 = it.letfTopCoor[0]
                y1 = it.letfTopCoor[1]
                x2 = it.rightBotCoor[0]
                y2 = it.rightBotCoor[1]
                rectF = RectF(x1, y1, x2, y2)
                if (rectF.contains(xTouch, yTouch)) {
                    return it.type
                }
            }
        }
        return null
    }
}