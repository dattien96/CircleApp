package com.datnht.circleapp

import android.app.Application
import com.datnht.circleapp.newsource.GameJsonLocator

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        GameJsonLocator.convertBaseJsonToDevice(this)
    }
}