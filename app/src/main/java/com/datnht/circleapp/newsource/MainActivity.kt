package com.datnht.circleapp.newsource

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.datnht.circleapp.R
import com.datnht.circleapp.screen.StartFragment
import com.datnht.circleapp.utils.buildLoopMediaSource
import com.datnht.circleapp.utils.initPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.video.VideoListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var player: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNormalPlayerList()
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.container, StartFragment())
//            .commitNow()
//        return
        touch_view?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent): Boolean {
                if (p1.actionMasked == 0) {
                    val ms = player!!.currentPosition
                    val result = GameJsonLocator.checkPointInRectF(p1.x, p1.y, ms)
                    if (result != null) {
                        Log.d("asdasd", "click trung ${result.name}")
                    } else {
                        Log.d("asdasd", "truot mnr, ms = $ms")
                    }
                }
                return true
            }

        })
    }

    private fun initNormalPlayerList() {
        video_view.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        player = video_view?.initPlayer(this)
        val dataSpec = DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.game))
        val rawResourceDataSource = RawResourceDataSource(this)
        rawResourceDataSource.open(dataSpec)

        val uri = RawResourceDataSource.buildRawResourceUri(R.raw.game)

        val mediaSource =
            buildLoopMediaSource(this ?: return, uri)
        player?.apply {
            playWhenReady = true
            addVideoListener(object : VideoListener {
                override fun onVideoSizeChanged(
                    width: Int,
                    height: Int,
                    unappliedRotationDegrees: Int,
                    pixelWidthHeightRatio: Float
                ) {
                    val p: ViewGroup.LayoutParams = main_root.getLayoutParams()
                    val currWidth: Int = video_view.getWidth()

                    // Set new width/height of view
                    // height or width must be cast to float as int/int will give 0
                    // and distort view, e.g. 9/16 = 0 but 9.0/16 = 0.5625.
                    // p.height is int hence the final cast to int.

                    // Set new width/height of view
                    // height or width must be cast to float as int/int will give 0
                    // and distort view, e.g. 9/16 = 0 but 9.0/16 = 0.5625.
                    // p.height is int hence the final cast to int.
                    p.width = currWidth
                    p.height = (height.toFloat() / width * currWidth).toInt()

                    // Redraw myView

                    // Redraw myView
                    video_view.requestLayout()
                }
            })
            prepare(mediaSource, false, false)

        }
    }
}