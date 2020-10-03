package com.datnht.circleapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.datnht.circleapp.screen.StartFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, StartFragment())
            .commitNow()
    }
}