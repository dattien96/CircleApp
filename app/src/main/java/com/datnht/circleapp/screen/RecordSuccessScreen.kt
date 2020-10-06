package com.datnht.circleapp.screen

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.datnht.circleapp.R
import com.datnht.circleapp.`object`.type.AppMaterialIdLocator
import com.datnht.circleapp.newsource.*
import com.datnht.circleapp.utils.buildLoopMediaSource
import com.datnht.circleapp.utils.initPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.android.exoplayer2.video.VideoListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_success_record.*

class RecordSuccessScreen : BaseFragment() {
    override val layoutId: Int
        get() = R.layout.fragment_success_record

    var player: SimpleExoPlayer? = null

    val handler = Handler()
    var runnable: Runnable? = null
    val runnable1 = Runnable {
        handler.postDelayed(runnable ?: return@Runnable, 500)
    }
    var isNewFrame = true
    var pointA: MutableList<Float>? = null
    var pointB: MutableList<Float>? = null
    var check = 1
    var items = mutableListOf<Item>()
    var startTime = 0L
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        startPlayButtonAnimation()
        button_start.setOnClickListener {
//            replaceFragment(PlayFragment(), PlayFragment.TAG, true)
            startPlayer()
        }
        btn_frame.setOnClickListener {
            val tmp = startTime
            startTime += 500
            var root = SuccessCoordinate(
                tmp,
                mutableListOf<Item>().apply { addAll(items) })
            items.clear()
            AppMaterialIdLocator.sucessRoot.add(root)
        }
        btn_again.setOnClickListener {
            var type = ItemJsonType.PLAY_AGAIN
            var item =
                Item(type, pointA!!, pointB!!)
            items.add(item)
        }
        btn_change_gift.setOnClickListener {
            var type = ItemJsonType.CHANGE_GIFT
            var item =
                Item(type, pointA!!, pointB!!)
            items.add(item)
        }

        btn_convert.setOnClickListener {
            val gson = Gson()
            val x = gson.toJson(AppMaterialIdLocator.sucessRoot)
            Log.d("asdasd", "$x")
        }

        touch_view?.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent): Boolean {
                if (p1.action == 0) {
                    if (check <= 2) {
                        when (check) {
                            1 -> {

                                pointA = mutableListOf<Float>().apply {
                                    add(p1.x)
                                    add(p1.y)
                                }
                                check++
                            }
                            2 -> {
                                pointB = mutableListOf<Float>().apply {
                                    add(p1.x)
                                    add(p1.y)
                                }
                                check = 1
                            }
                        }
                    } else {
                        check = 1
                    }
                    val ms = player!!.currentPosition

                }
                return true
            }

        })
        initNormalPlayerList()
        runnable = Runnable {
            pausePlayer()
        }
        runnable1.run()
    }

    private fun pausePlayer() {
        player!!.playWhenReady = false
        player!!.playbackState
    }

    private fun startPlayer() {
        player!!.playWhenReady = true
        player!!.playbackState
    }

    private fun startPlayButtonAnimation() {
        ObjectAnimator.ofFloat(1f, 0.5f, 0f, 1f).apply {
            duration = 1000
            addUpdateListener { animator ->
                button_start?.alpha = animator.animatedValue as Float
            }
            repeatCount = ObjectAnimator.INFINITE
            start()
        }
    }

    private fun initNormalPlayerList() {
        video_view.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        player = video_view?.initPlayer(this@RecordSuccessScreen.context ?: return)
        val dataSpec = DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.bird_success))
        val rawResourceDataSource = RawResourceDataSource(context)
        rawResourceDataSource.open(dataSpec)

        val uri = RawResourceDataSource.buildRawResourceUri(R.raw.bird_success)

        val mediaSource =
            buildLoopMediaSource(this@RecordSuccessScreen.context ?: return, uri)
        player?.apply {
            playWhenReady = true
            addListener(object : Player.EventListener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        runnable1.run()
                    }
                }
            })
            addVideoListener(object : VideoListener {
                override fun onVideoSizeChanged(
                    width: Int,
                    height: Int,
                    unappliedRotationDegrees: Int,
                    pixelWidthHeightRatio: Float
                ) {
                    val p: ViewGroup.LayoutParams = constrain_root.getLayoutParams()
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