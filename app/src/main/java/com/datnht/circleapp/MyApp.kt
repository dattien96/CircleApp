package com.datnht.circleapp

import android.app.Application
import com.datnht.circleapp.newsource.GameJsonLocator
import com.datnht.circleapp.newsource.IntroJsonLocator
import com.datnht.circleapp.newsource.SuccessJsonLocator

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        GameJsonLocator.convertBaseJsonToDevice(this)
        IntroJsonLocator.convertBaseJsonToDevice(this)
        SuccessJsonLocator.convertBaseJsonToDevice(this)
    }
}